/*
  */
package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response wrapper for responses of lists of data wf
 *
 * Created by mfriend on 2020-01-05.
 */
@JsonClass(generateAdapter = true)
data class Pager<T>(
    @Json(name = "href")
    val href: String,
    @Json(name = "items")
    val items: List<T>,
    @Json(name = "limit")
    val limit: Int,
    @Json(name = "next")
    val next: String?,
    @Json(name = "offset")
    val offset: Int,
    @Json(name = "previous")
    val previous: String?,
    @Json(name = "total")
    val total: Int
)