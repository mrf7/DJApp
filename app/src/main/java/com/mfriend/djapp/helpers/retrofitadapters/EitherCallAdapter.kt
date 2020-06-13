package com.mfriend.djapp.helpers.retrofitadapters

import arrow.core.Either
import arrow.core.left
import arrow.core.toOption
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
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Sealed class of all potential errors from the spotify api
 */
sealed class ErrorResponse<out T> {
    data class ApiError<out T>(val body: T, val code: Int) : ErrorResponse<T>()
    data class NetworkError(val error: IOException) : ErrorResponse<Nothing>()
    data class UnknownError(val error: Throwable?) : ErrorResponse<Nothing>()
}


/**
 * Wraps a [delegate] from retrofit to return an [Either.left] of [ErrorResponse] instead of throwing an exception in
 * all failure cases
 */
@Suppress("UndocumentedPublicFunction")
class EitherResponseCall<T : Any, E : Any>(
    private val delegate: Call<T>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<Either<ErrorResponse<E>, T>> {

    override fun enqueue(callback: Callback<Either<ErrorResponse<E>, T>>) =
        delegate.enqueue(EitherCallbackDecorator(callback, this, errorConverter))

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun clone(): Call<Either<ErrorResponse<E>, T>> = EitherResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<Either<ErrorResponse<E>, T>> {
        throw UnsupportedOperationException("Can't execute this guy")
    }

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}

/**
 * Decorator for a callback to convert callbacks of [T] that throw exceptions on error responses to a Callback of [Either]
 * where [Either.left] is an [ErrorResponse] that may have an expected error body of [E] and [Either.Right] is the
 * successful response body
 *
 * @param underlyingCallback the [Callback] listening for events
 * @param errorConverter to convert the error response body into [E]
 * @param eitherCall to use in the callback methods
 */
internal class EitherCallbackDecorator<T, E>(
    private val underlyingCallback: Callback<Either<ErrorResponse<E>, T>>,
    private val eitherCall: Call<Either<ErrorResponse<E>, T>>,
    private val errorConverter: Converter<ResponseBody, E>
) : Callback<T> {
    /**
     * This is called when a network response is received. Can either be a 2XX (success) or
     * a failure status code
     */
    override fun onResponse(call: Call<T>, response: Response<T?>) {
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
            @Suppress("UseIfInsteadOfWhen")
            val convertedError = when {
                errorBody == null || errorBody.contentLength() == 0L -> null
                else -> kotlin.runCatching {
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
        underlyingCallback.onResponse(eitherCall, Response.success(responseResult))
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        @Suppress("UseIfInsteadOfWhen")
        val networkResponse = when (t) {
            is IOException -> ErrorResponse.NetworkError(t)
            else -> ErrorResponse.UnknownError(t)
        }.left()
        // I think using onResponse and Response.success() instead of onFailure here prevents
        // the exception from getting thrown to the caller and instead just delivers it raw
        underlyingCallback.onResponse(eitherCall, Response.success(networkResponse))
    }
}

/**
 * Adapts a [Call] to an [EitherResponseCall]
 */
class EitherResponseCallAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<Either<ErrorResponse<E>, S>>> {
    /**
     * Adapts
     */
    override fun adapt(call: Call<S>): Call<Either<ErrorResponse<E>, S>> =
        EitherResponseCall(call, errorBodyConverter)

    /**
     * The type of the successful response body
     */
    override fun responseType(): Type = successType
}

/**
 * Used by retrofit to try to get a custom calladapter for a given response type
 */
class EitherResponseAdapterFactory : CallAdapter.Factory() {
    /**
     * Returns an [EitherResponseCallAdapter] to satisfy the [returnType] if possible.
     * Otherwise either returns null or throws an exception, both of which are treated the same by retrofit.
     */
    @Suppress("ReturnCount")
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
        val leftType = getParameterUpperBound(0, responseType)
        // If left type isn't ErrorResponse, or isnt parameterized, we cant do anything
        if (getRawType(leftType) != ErrorResponse::class.java) {
            return null
        }
        check(leftType is ParameterizedType) {
            "Left type must be parameterized as ErrorResponse<ExpectedErrorBodyType>"
        }
        val expectedErrorBodyType = getParameterUpperBound(0, leftType)

        val rightType = getParameterUpperBound(1, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, expectedErrorBodyType, annotations)

        return EitherResponseCallAdapter<Any, Any>(rightType, errorBodyConverter)
    }
}