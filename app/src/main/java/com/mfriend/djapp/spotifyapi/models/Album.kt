package com.mfriend.djapp.spotifyapi.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "artists")
    val artists: List<Artist>,
    @Json(name = "href")
    val href: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "images")
    val images: List<Image>,
    @Json(name = "name")
    val name: String,
    @Json(name = "uri")
    val uri: String
)