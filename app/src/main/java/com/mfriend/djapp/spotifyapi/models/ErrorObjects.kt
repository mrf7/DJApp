package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json

// Error objects defined by https://developer.spotify.com/documentation/web-api/
/**
 * Generic API error response body
 *
 * @property status an HTTP status code defined by https://developer.spotify.com/documentation/web-api/#response-status-codes
 * @property message short description of the error cause
 */
data class SpotifyErrorBody(
    @Json(name = "status")
    val status: Int,
    @Json(name = "message")
    val message: String
)