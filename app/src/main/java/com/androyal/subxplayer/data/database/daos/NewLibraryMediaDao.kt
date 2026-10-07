package com.androyal.subxplayer.data.database.daos
import androidx.room.*
import com.androyal.subxplayer.data.database.tables.NewLibraryMedia
import kotlinx.coroutines.flow.Flow

@Dao
interface NewLibraryMediaDao {
    @Query("SELECT * FROM newlibrarymedia ORDER BY id DESC")
    fun observeAll(): Flow<List<NewLibraryMedia>>

    @Query("SELECT * FROM newlibrarymedia")
    suspend fun getAll(): List<NewLibraryMedia>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: NewLibraryMedia): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<NewLibraryMedia>)

    @Update
    suspend fun update(item: NewLibraryMedia)

    @Delete
    suspend fun delete(item: NewLibraryMedia)

    @Query("DELETE FROM newlibrarymedia WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM newlibrarymedia")
    suspend fun clearAll()
}
