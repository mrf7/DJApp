package com.mfriend.djapp.spotifyapi

import com.mfriend.djapp.spotifyapi.adapters.EitherResponseAdapterFactory
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Koin module to provide the spotify api via the [SpotifyApi] interface
 *
 * Created by MFriend on 2020-01-05.
 */
object SpotifyModule {
    private const val SPOTIFY_WEB_API_URL = "https://api.spotify.com/v1/"
    private const val REDIRECT_URI = "http://com.mfriend.djapp/callback"
    private const val CLIENT_ID = "bc77027fdfd54c0091c11fcc1895c5dd"

    private fun retrofit(authToken: String): Retrofit {
        return Retrofit.Builder()
            .client(getClient(authToken))
            .baseUrl(SPOTIFY_WEB_API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(EitherResponseAdapterFactory())
            .build()
    }

    private fun getClient(authToken: String): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")
                    .build()
                chain.proceed(request)
            }
//            val logger =
//                HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }
//            addInterceptor(logger)
        }.build()
    }

    fun getRequest(): AuthenticationRequest {
        return AuthenticationRequest.Builder(
            CLIENT_ID,
            AuthenticationResponse.Type.TOKEN,
            REDIRECT_URI
        )
            .run {
                setScopes(
                    arrayOf(
                        "user-library-modify",
                        "user-library-read",
                        "app-remote-control",
                        "playlist-read-collaborative",
                        "playlist-read-private",
                        "playlist-modify-public",
                        "playlist-modify-private",
                        "user-top-read"
                    )
                )
                build()
            }
    }

    fun get() = module {
        single {
            val authToken: String =
                requireNotNull(getKoin().getProperty("authToken")) { " Auth token not set" }
            retrofit(authToken).create(SpotifyApi::class.java)
        }
    }
}