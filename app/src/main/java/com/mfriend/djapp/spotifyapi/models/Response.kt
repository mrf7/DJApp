/*
  */
package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json

/**
 * Created by mfriend on 2020-01-05.
 * TODO mfriend WRITE CLASS HEADER
 */
data class Response<T>(
    @Json(name = "href")
    val href: String,
    @Json(name = "items")
    val items: List<T>
)