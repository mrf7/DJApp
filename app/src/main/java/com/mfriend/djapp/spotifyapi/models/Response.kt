/*
  */
package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json

/**
 * Response wrapper for responses of lists of data wf
 *
 * Created by mfriend on 2020-01-05.
 */
data class Response<T>(
    @Json(name = "href")
    val href: String,
    @Json(name = "items")
    val items: List<T>
)