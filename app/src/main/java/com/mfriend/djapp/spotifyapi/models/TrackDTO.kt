package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response data hold for the spotify api's track object
 *
 *
 */
@JsonClass(generateAdapter = true)
data class TrackDTO(
    @Json(name = "album")
    val album: Album,
    @Json(name = "artists")
    val artists: List<Artist>,
    @Json(name = "href")
    val href: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "preview_url")
    val previewUrl: String,
    @Json(name = "uri")
    val uri: String
)