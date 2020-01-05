package com.mfriend.djapp.spotifyapi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * TODO MRF Write Class header
 *
 * Created by MFriend on 2020-01-05.
 * Copyright (c) 2020 Lutron. All rights reserved.
 */
object ApiFactory {
    private const val SPOTIFY_WEB_ABI_URL = "https://api.spotify.com/v1/"
    private fun retrofit(authToken: String): Retrofit = Retrofit.Builder().apply {
        client(getClient(authToken))
        baseUrl(SPOTIFY_WEB_ABI_URL)
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
        }.build()
    }

    fun getSpotifyService(authToken: String): SpotifyService =
        retrofit(authToken).create(SpotifyService::class.java)
}