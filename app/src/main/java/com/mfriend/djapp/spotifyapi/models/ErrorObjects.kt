package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json

data class AuthError(
    @Json(name = "error")
    val error: String,
    @Json(name = "error_description")
    val error_description: String
)