package com.mfriend.djapp.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mfriend.djapp.common.db.daos.TrackDao
import com.mfriend.djapp.common.db.entities.Track

/**
 * Abstract database class for room to generate db
 */
@Database(entities = [Track::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Gets a [TrackDao] instance to interact with the [Track] table
     */
    abstract fun trackDao(): TrackDao

    companion object {
        /**
         * Builds uses room to create the database
         *
         * @param context app context to create the database
         */
        fun buildDb(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "AppDb")
                .fallbackToDestructiveMigration()
                .build()
    }
}