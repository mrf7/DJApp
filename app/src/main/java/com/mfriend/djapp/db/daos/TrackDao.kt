package com.mfriend.djapp.db.daos

import androidx.room.*
import com.mfriend.djapp.db.entities.Track

@Dao
interface TrackDao {
    @Query("SELECT * FROM Track")
    suspend fun getAll(): List<Track>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg track: Track)

    @Delete
    suspend fun delete(track: Track)
}