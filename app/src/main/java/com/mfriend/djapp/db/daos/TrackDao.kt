package com.mfriend.djapp.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mfriend.djapp.db.entities.Track

@Dao
interface TrackDao {

    @Insert
    suspend fun insert(vararg tracks: Track)

    @Query("SELECT * FROM track")
    fun getAll(): List<Track>
}
