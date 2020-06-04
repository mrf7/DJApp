package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents an Album object in the spotify api
 *
 * @property artists a list of the artists credited
 * @property href to fetch this album from the api
 * @property id the spotify defined id for the album
 * @property name the name of the ablum
 * @property uri the spotify uri to refer to the ablbum
 */
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