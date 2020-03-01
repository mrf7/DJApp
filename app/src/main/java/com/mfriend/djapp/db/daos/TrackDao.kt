package com.mfriend.djapp.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mfriend.djapp.db.entities.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM Track")
    suspend fun getAll(): List<Track>

    @Query("SELECT * FROM Track")
    fun getAllFlow(): Flow<List<Track>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg track: Track)

    @Delete
    suspend fun delete(track: Track)
}