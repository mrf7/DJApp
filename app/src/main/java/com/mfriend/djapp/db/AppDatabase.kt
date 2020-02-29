/*
 * Copyright (c) 2020 Lutron. All rights reserved.
 */
package com.mfriend.djapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mfriend.djapp.db.daos.TrackDao
import com.mfriend.djapp.db.entities.Track

/**
 * Created by mfriend on 2020-02-29.
 *
 */
@Database(entities = [Track::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

    companion object {
        fun buildDb(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }
}