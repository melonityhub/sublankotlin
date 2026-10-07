package com.androyal.subxplayer.data.database.daos
import androidx.room.*
import com.androyal.subxplayer.data.database.tables.MediaLibraryIndex
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaLibraryIndexDao {
    @Query("SELECT * FROM medialibraryindex ORDER BY id DESC")
    fun observeAll(): Flow<List<MediaLibraryIndex>>

    @Query("SELECT * FROM medialibraryindex")
    suspend fun getAll(): List<MediaLibraryIndex>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MediaLibraryIndex): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MediaLibraryIndex>)

    @Update
    suspend fun update(item: MediaLibraryIndex)

    @Delete
    suspend fun delete(item: MediaLibraryIndex)

    @Query("DELETE FROM medialibraryindex WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM medialibraryindex")
    suspend fun clearAll()
}
