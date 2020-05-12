package com.mfriend.djapp.spotifyapi.models

import com.squareup.moshi.Json

data class PlaylistSnapshot(
    @Json(name = "snapshot_id")
    val snapshotId: String
)