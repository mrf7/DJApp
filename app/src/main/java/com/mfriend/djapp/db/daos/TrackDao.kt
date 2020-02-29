package com.mfriend.djapp.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mfriend.djapp.db.entities.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert
    suspend fun insert(vararg tracks: Track)

    @Query("SELECT * FROM track")
    fun getAll(): Flow<List<Track>>

    @Delete
    suspend fun delete(track: Track)
}
