package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response data holder for the spotify api's representation of an image
 *
 * @property url to fetch the image from
 * @property height of the image in pixels or null if unknown
 * @property width of the image in pixels or null if unknown
 */
@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "height")
    val height: Int?,
    @Json(name = "url")
    val url: String,
    @Json(name = "width")
    val width: Int?
)