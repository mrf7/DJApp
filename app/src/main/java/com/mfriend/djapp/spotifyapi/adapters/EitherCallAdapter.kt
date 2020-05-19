package com.mfriend.djapp.spotifyapi.adapters

import arrow.core.Either
import arrow.core.Option
import arrow.core.left
import arrow.core.toOption
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import okhttp3.Request
import okhttp3.ResponseBody
import okio.IOException
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Sealed class of all potential errors from the spotify api
 */
sealed class ErrorResponse<out T> {
    data class ApiError<T>(val body: T, val code: Int) : ErrorResponse<T>()
    data class NetworkError(val error: IOException) : ErrorResponse<Nothing>()
    data class UnknownError(val error: Throwable?) : ErrorResponse<Nothing>()
}

class EitherResponseCall<T : Any, E : Any>(
    private val delegate: Call<T>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<Either<ErrorResponse<E>, T>> {

    override fun enqueue(callback: Callback<Either<ErrorResponse<E>, T>>) {
        return delegate.enqueue(object : Callback<T> {
            // This is called when a network response is received. Can either be a 2XX (success) or
            // a failure status code
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val responseResult = if (response.isSuccessful) {
                    // Convert the T? response body into Option<T> then into Either<ErrorResponse, T>
                    // gives left is response.body() is null, and response.body().right otherwise
                    response.body().toOption()
                        .toEither(ifEmpty = { ErrorResponse.UnknownError(null) })
                } else {
                    // get the response body of an error
                    val errorBody = response.errorBody()
                    // Try to convert the error body into E with errorConverter, if that doesnt work
                    // then give null. Convert E? into Option<E> with toOption()
                    val convertedError: Option<E> =
                        if (errorBody == null || errorBody.contentLength() == 0L) {
                            null
                        } else {
                            kotlin.runCatching {
                                errorConverter.convert(errorBody)
                            }.getOrNull()
                        }.toOption()

                    // If we were able to parse an errorBody into E, give an ApiError<E>, otherwise,
                    // give UnknownError. Wrap both branches into Either.left
                    convertedError.fold(
                        ifEmpty = { ErrorResponse.UnknownError(null) },
                        ifSome = { ErrorResponse.ApiError(it, response.code()) }
                    ).left()
                }
                // Notify callback of the response
                callback.onResponse(this@EitherResponseCall, Response.success(responseResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResponse: Either<ErrorResponse<Nothing>, Nothing> = if (t is IOException) {
                    ErrorResponse.NetworkError(t)
                } else {
                    ErrorResponse.UnknownError(t)
                }.left()
                // I think using onResponse and Response.success() instead of onFailure here prevents
                // the exception from getting thrown to the caller and instead just delivers it raw
                callback.onResponse(this@EitherResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun clone(): Call<Either<ErrorResponse<E>, T>> =
        EitherResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<Either<ErrorResponse<E>, T>> {
        throw UnsupportedOperationException("Can't execute this guy")
    }

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}

class EitherResponseCallAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<Either<ErrorResponse<E>, S>>> {
    override fun adapt(call: Call<S>): Call<Either<ErrorResponse<E>, S>> =
        EitherResponseCall(call, errorBodyConverter)

    override fun responseType(): Type = successType
}

class EitherResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Suspend functions wrap type in Call
        if (Call::class.java != getRawType(returnType)) {
            return null
        }
        // Make sure its parameterized
        check(returnType is ParameterizedType) {
            "return type must be Call<Either<ErrorResponse,T>>"
        }
        // Get response type inside Call
        val responseType = getParameterUpperBound(0, returnType)
        // If its not Either, then we cant handle it
        if (getRawType(responseType) != Either::class.java) {
            return null
        }

        // the response type is Either and should be parameterized
        check(responseType is ParameterizedType) {
            "Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>"
        }

        // In Either, the second parameterized type is the success, first is error
        val errorBodyType = getParameterUpperBound(0, responseType)
        val successBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

        return EitherResponseCallAdapter<Any, Any>(successBodyType, errorBodyConverter)
    }
}