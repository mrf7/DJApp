package com.mfriend.djapp.typeconverters

import com.mfriend.djapp.common.db.entities.Track
import com.mfriend.djapp.spotifyapi.models.TrackDTO

/**
 * Convert a [TrackDTO] to a [Track] db object
 */
fun TrackDTO.toTrack(): Track =
    Track(
        name,
        artists.first().name,
        album.name,
        uri,
        album.images.first().url
    )