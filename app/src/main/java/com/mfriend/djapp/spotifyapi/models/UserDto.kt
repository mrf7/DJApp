package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "display_name")
    val displayName: String,
    @Json(name = "id")
    val id: String
)
