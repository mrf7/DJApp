package com.mfriend.djapp.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Track(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "album") val album: String
) {
    // Auto generated  values so they dont get used in equals, toString, or hashCode
    @PrimaryKey(autoGenerate = true) val id: Int = 0
}