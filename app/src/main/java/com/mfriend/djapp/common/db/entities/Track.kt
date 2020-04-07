package com.mfriend.djapp.common.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Track(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "album") val album: String,
    @ColumnInfo(name = "spotifyUri") val spotifyUri: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String
) {
    // Auto generated  values so they dont get used in equals, toString, or hashCode
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}