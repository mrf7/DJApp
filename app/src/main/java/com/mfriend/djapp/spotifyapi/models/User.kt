package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "display_name")
    val displayName: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "images")
    val images: List<String>
)

