package com.androyal.subxplayer.data.database.daos
import androidx.room.*
import com.androyal.subxplayer.data.database.tables.VideoCache
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoCacheDao {
    @Query("SELECT * FROM videocache ORDER BY id DESC")
    fun observeAll(): Flow<List<VideoCache>>

    @Query("SELECT * FROM videocache")
    suspend fun getAll(): List<VideoCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: VideoCache): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<VideoCache>)

    @Update
    suspend fun update(item: VideoCache)

    @Delete
    suspend fun delete(item: VideoCache)

    @Query("DELETE FROM videocache WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM videocache")
    suspend fun clearAll()
}
