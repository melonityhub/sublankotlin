package com.androyal.subxplayer.data.database.daos
import androidx.room.*
import com.androyal.subxplayer.data.database.tables.TranslationCache
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationCacheDao {
    @Query("SELECT * FROM translationcache ORDER BY id DESC")
    fun observeAll(): Flow<List<TranslationCache>>

    @Query("SELECT * FROM translationcache")
    suspend fun getAll(): List<TranslationCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TranslationCache): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TranslationCache>)

    @Update
    suspend fun update(item: TranslationCache)

    @Delete
    suspend fun delete(item: TranslationCache)

    @Query("DELETE FROM translationcache WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM translationcache")
    suspend fun clearAll()
}
