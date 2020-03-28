package com.mfriend.djapp.common.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfriend.djapp.common.db.entities.Track
import kotlinx.coroutines.flow.Flow

/**
 * Dao with methods to access the [Track] table in the database
 */
@Dao
interface TrackDao {
    /**
     * @return all [Track] objects in the database as a one shot suspending request
     */
    @Query("SELECT * FROM Track")
    suspend fun getAll(): List<Track>

    /**
     * @return all [Track] objects in the database as a [List] wrapped in a [Flow]. The flow will update
     * whenever any changes are made to the [Track] table
     */
    @Query("SELECT * FROM Track")
    fun getAllFlow(): Flow<List<Track>>

    /**
     * Inserts one more more  [Track]s from the [track] vararg, replacing existing entries on conflicts
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg track: Track)

    /**
     * Deletes [track] from the database
     */
    @Delete
    suspend fun delete(track: Track)
}