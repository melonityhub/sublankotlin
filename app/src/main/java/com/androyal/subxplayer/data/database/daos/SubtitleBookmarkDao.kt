package com.androyal.subxplayer.data.database.daos
import androidx.room.*
import com.androyal.subxplayer.data.database.tables.SubtitleBookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtitleBookmarkDao {
    @Query("SELECT * FROM subtitlebookmark ORDER BY id DESC")
    fun observeAll(): Flow<List<SubtitleBookmark>>

    @Query("SELECT * FROM subtitlebookmark")
    suspend fun getAll(): List<SubtitleBookmark>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SubtitleBookmark): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SubtitleBookmark>)

    @Update
    suspend fun update(item: SubtitleBookmark)

    @Delete
    suspend fun delete(item: SubtitleBookmark)

    @Query("DELETE FROM subtitlebookmark WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM subtitlebookmark")
    suspend fun clearAll()
}
