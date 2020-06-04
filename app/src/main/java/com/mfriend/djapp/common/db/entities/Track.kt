package com.mfriend.djapp.common.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Spotify Track in the database
 *
 * @property name of the track
 * @property artist the main artist of the track as a string
 * @property album name of the track
 * @property spotifyUri the unique uri representing the track in spotify
 * @property imageUrl to retrieve an image of the album artwork
 */
@Entity
data class Track(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "album") val album: String,
    @ColumnInfo(name = "spotifyUri") val spotifyUri: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String
) {
    // Auto generated  values so they dont get used in equals, toString, or hashCode
    /**
     * @property id unique id representing this object in the DB. Will be 0 if this does not exist in the database, nonzero otherwise
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}