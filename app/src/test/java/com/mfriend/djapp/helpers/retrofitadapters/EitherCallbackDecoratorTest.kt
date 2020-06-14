package com.mfriend.djapp.helpers.retrofitadapters

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import java.io.IOException
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response

private typealias EitherResponse = Either<ErrorResponse<String>, String>

internal class EitherCallbackDecoratorTest {
    private val mockCallback: Callback<EitherResponse> = mockk()
    private val mockEitherCall: Call<EitherResponse> = mockk()
    private val mockCall: Call<String> = mockk()
    private val mockConverter: Converter<ResponseBody, String> = mockk()
    private val callbackUnderTest =
        EitherCallbackDecorator(mockCallback, mockEitherCall, mockConverter)

    @BeforeEach
    fun setUp() {
        clearMocks(mockEitherCall, mockCallback)
        // Every result should be delivered via onResponse, not onFailure
        every { mockCallback.onResponse(refEq(mockEitherCall), any()) } just runs
    }

    @Nested
    inner class SuccessfulResponse {
        @Test
        fun `successful response with valid body`() {
            // When  a successful response comes
            val expectedResponse = Response.success("gud")
            callbackUnderTest.onResponse(mockCall, expectedResponse)

            // then we should get a callback with Either.Right("gud")
            val responseSlot = slot<Response<EitherResponse>>()
            verify {
                mockCallback.onResponse(mockEitherCall, capture(responseSlot))
            }
            val actualResponse = responseSlot.captured.body() ?: fail("Got null response body")
            val responseBody = actualResponse.getOrElse { fail("Got left response, expected right") }
            assertThat(responseBody).isEqualTo("gud")
        }

        @Test
        fun `success response body null`() {
            // when a response with null body comes
            val nullResponse: Response<String?> = Response.success(null)
            callbackUnderTest.onResponse(mockCall, nullResponse)

            val responseSlot = slot<Response<EitherResponse>>()
            verify {
                mockCallback.onResponse(mockEitherCall, capture(responseSlot))
            }
            val actualResponse = responseSlot.captured.body() ?: fail("Got null response body instead of either")
            assertThat(actualResponse).isEqualTo(ErrorResponse.UnknownError(null).left())
        }
    }

    @Nested
    inner class ErrorResponses {
        @Test
        fun `error response expected body structure`() {
            // When called with a non successful response but expected error body
            // give an expected converted error body value
            every { mockConverter.convert(any()) } returns "E"
            val errorResponse = Response.error<String>(420, "{E}".toResponseBody())
            callbackUnderTest.onResponse(mockCall, errorResponse)
            val responseSlot = slot<Response<EitherResponse>>()
            verify {
                mockCallback.onResponse(mockEitherCall, capture(responseSlot))
                mockConverter.convert(any())
            }
            val actualResponse = responseSlot.captured.body() ?: fail("Got null response body")
            assertThat(actualResponse).isEqualTo(ErrorResponse.ApiError("E", 420).left())
        }

        @Test
        fun `error response unexpected body structure`() {
            // When called with a non successful response but expected error body
            // give an expected converted error body value
            every { mockConverter.convert(any()) } throws IOException()
            val errorResponse = Response.error<String>(420, "{E}".toResponseBody())
            callbackUnderTest.onResponse(mockCall, errorResponse)
            val responseSlot = slot<Response<EitherResponse>>()
            verify {
                mockCallback.onResponse(mockEitherCall, capture(responseSlot))
                mockConverter.convert(any())
            }
            val actualResponse = responseSlot.captured.body() ?: fail("Got null response body")
            assertThat(actualResponse).isEqualTo(ErrorResponse.UnknownError(null).left())
        }

        @Test
        fun `failure network error`() {
            // Given an IOException
            val except = IOException()
            // When an IOException occurs
            callbackUnderTest.onFailure(mockCall, except)

            val responseSlot = slot<Response<EitherResponse>>()
            verify {
                mockCallback.onResponse(mockEitherCall, capture(responseSlot))
            }
            val actualResponse = responseSlot.captured.body()
            assertThat(actualResponse).isEqualTo(ErrorResponse.NetworkError(except).left())
        }

        @Test
        fun `failure other error`() {
            // Given an IOException
            val except = ClassCastException()
            // When an IOException occurs
            callbackUnderTest.onFailure(mockCall, except)

            val responseSlot = slot<Response<EitherResponse>>()
            verify {
                mockCallback.onResponse(mockEitherCall, capture(responseSlot))
            }
            val actualResponse = responseSlot.captured.body()
            assertThat(actualResponse).isEqualTo(ErrorResponse.UnknownError(except).left())
        }
    }
}