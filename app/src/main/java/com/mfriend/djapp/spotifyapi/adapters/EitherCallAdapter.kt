package com.mfriend.djapp.spotifyapi.adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.toOption
import okhttp3.Request
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Sealed class of all potential errors from the spotify api
 */
sealed class ErrorResponse {
    data class ApiError<U : Any>(val body: U, val code: Int) : ErrorResponse()
    data class NetworkError(val error: IOException) : ErrorResponse()
    data class UnknownError(val error: Throwable?) : ErrorResponse()
}


class EitherResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<Either<ErrorResponse, S>> {

    override fun enqueue(callback: Callback<Either<ErrorResponse, S>>) {
        return delegate.enqueue(object : Callback<S> {
            // This is called when a network response is received. Can either be a 2XX (success) or
            // a failure status code
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val responseResult = if (response.isSuccessful) {
                    // For status codes, give the body if non null, otherwise give unknown error
                    response.body().toOption().toEither {
                        ErrorResponse.UnknownError(null)
                    }
                } else {
                    val error = response.errorBody()
                    // Convert the error body into the appropriate ApiError data class via the converter
                    // or null
                    val convertedError = when {
                        error == null || error.contentLength() == 0L -> null
                        else -> kotlin.runCatching {
                            errorConverter.convert(error)
                        }.getOrNull()
                    }.toOption()
                    // wrap the error in the appropriate ErrorResponse type based on if we have an error
                    // body
                    convertedError.fold(
                        ifEmpty = { ErrorResponse.UnknownError(null) },
                        ifSome = { ErrorResponse.ApiError(convertedError, response.code()) }
                    ).left()
                }
                // Notify the callback of the response
                callback.onResponse(this@EitherResponseCall, Response.success(responseResult))
            }

            override fun onFailure(call: Call<S>, t: Throwable) {
                val networkResponse = when (t) {
                    is IOException -> ErrorResponse.NetworkError(t)
                    else -> ErrorResponse.UnknownError(t)
                }.left()
                // I think using onResponse and Response.success() instead of onFailure here prevents
                // the exception from getting thrown to the caller and instead just delivers it raw
                callback.onResponse(this@EitherResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun clone(): Call<Either<ErrorResponse, S>> =
        EitherResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<Either<ErrorResponse, S>> {
        throw UnsupportedOperationException("Can't execute this guy")
    }

    override fun request(): Request = delegate.request()
}

class EitherResponseCallAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<Either<ErrorResponse, S>>> {
    override fun adapt(call: Call<S>): Call<Either<ErrorResponse, S>> {
        return EitherResponseCall(call, errorBodyConverter)
    }

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
        check(responseType is ParameterizedType) { "Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>" }

        val successBodyType = getParameterUpperBound(0, responseType)
        val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

        return EitherResponseCallAdapter<Any, Any>(successBodyType, errorBodyConverter)
    }
}