package com.mfriend.djapp.spotifyapi.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Playlist(
    @Json(name = "description")
    val description: String?,
    @Json(name = "href")
    val href: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "public")
    val isPublic: Boolean,
    @Json(name = "uri")
    val uri: String
) : Serializable

@JsonClass(generateAdapter = true)
data class PlaylistDTO(
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "public")
    val isPublic: Boolean = false
)