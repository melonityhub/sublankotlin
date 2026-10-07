package com.androyal.subxplayer.data.database.daos
import androidx.room.*
import com.androyal.subxplayer.data.database.tables.PlaybackState
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaybackStateDao {
    @Query("SELECT * FROM playbackstate ORDER BY id DESC")
    fun observeAll(): Flow<List<PlaybackState>>

    @Query("SELECT * FROM playbackstate")
    suspend fun getAll(): List<PlaybackState>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PlaybackState): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PlaybackState>)

    @Update
    suspend fun update(item: PlaybackState)

    @Delete
    suspend fun delete(item: PlaybackState)

    @Query("DELETE FROM playbackstate WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM playbackstate")
    suspend fun clearAll()
}
