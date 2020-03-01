package com.mfriend.djapp.spotifyapi

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * TODO MRF Write Class header
 *
 * Created by MFriend on 2020-01-05.
 */
object SpotifyModule {
    private const val SPOTIFY_WEB_API_URL = "https://api.spotify.com/v1/"
    private fun retrofit(authToken: String): Retrofit = Retrofit.Builder().apply {
        client(getClient(authToken))
        baseUrl(SPOTIFY_WEB_API_URL)
        addConverterFactory(MoshiConverterFactory.create())
    }.build()

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

    fun get() = module {
        single {
            val authToken: String = getKoin().getProperty("authToken")
                ?: throw IllegalStateException("Auth token not set")
            retrofit(authToken).create(SpotifyService::class.java)
        }
    }
}