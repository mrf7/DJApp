/**
 * TODO MRF Write Class header
 *
 * Created by MFriend on 2020-02-29.
 * Copyright (c) 2020 Lutron. All rights reserved.
 */
package com.mfriend.djapp.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Track(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "album") val album: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)