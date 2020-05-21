package com.mfriend.djapp.spotifyapi.adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.toOption
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Sealed class of all potential errors from the spotify api
 */
sealed class ErrorResponse<out T> {
    /**
     * Returned when the request responds with an errorobject from api with a defined error [body] of
     * type [T] with http error code [code]
     */
    data class ApiError<T>(val body: T, val code: Int) : ErrorResponse<T>()

    /**
     * Returned when a network error occurred that prevent a response from being delivered
     *
     * @property error that occurred
     */
    data class NetworkError(val error: IOException) : ErrorResponse<Nothing>()

    /**
     * Returned when some unknown error occurs
     *
     * @property error the error that occurred
     */
    data class UnknownError(val error: Throwable) : ErrorResponse<Nothing>()

    /**
     * Returned when the response is successful, but contained no body.
     *
     * @property code the success status code returned
     */
    data class EmptyBodyError(val code: Int) : ErrorResponse<Nothing>()
}

/**
 * A [Call] that returns an [Either] instead of throwing exceptions. [Either.left] will be an
 * [ErrorResponse] of some expected error body type [E], and [Either.right] will be the expected
 * success response body type. Non successful responses other than those with the expected error
 * body format will be returned as the appropriate subclass of [ErrorResponse].
 *
 * @param delegate the retrofit provided call delegate we use to make the call
 * @param errorConverter to convert the error [ResponseBody] to [E]
 */
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
                        .toEither(ifEmpty = { ErrorResponse.EmptyBodyError(response.code()) })
                } else {
                    // get the response body of an error
                    val errorBody = response.errorBody()
                    val responseCode = response.code()
                    try {
                        // If the errorBody isnt null, try to convert it. The convert may also return
                        // null so coalesce these nulls together.
                        val convertedBody = errorBody?.let { errorConverter.convert(it) }.toOption()
                        // If we're able to get an error body, return an api error. If the body is null
                        // return an empty body error
                        convertedBody.fold(
                            ifEmpty = { ErrorResponse.EmptyBodyError(responseCode) },
                            ifSome = { ErrorResponse.ApiError(it, responseCode) }
                        ).left()
                    } catch (ex: IOException) {
                        // If we get some exception parsing the error body (likely because it had an
                        // unexpected format and couldnt be parsed into E), return an unknown error
                        ErrorResponse.UnknownError(ex).left()
                    }
                }
                // Notify callback of the response
                callback.onResponse(this@EitherResponseCall, Response.success(responseResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResponse: Either<ErrorResponse<Nothing>, Nothing> =
                    if (t is IOException) {
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

/**
 * [CallAdapter] used by retrofit to wrap a call in [EitherResponseCall]
 */
class EitherResponseCallAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<Either<ErrorResponse<E>, S>>> {
    override fun adapt(call: Call<S>): Call<Either<ErrorResponse<E>, S>> =
        EitherResponseCall(call, errorBodyConverter)

    override fun responseType(): Type = successType
}

/**
 * [CallAdapter.Factory] for retrofit to determine if we can use [EitherResponseCall] to resolve a given return
 * type
 */
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