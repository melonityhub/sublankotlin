package com.androyal.subxplayer.data.database.daos
import androidx.room.*
import com.androyal.subxplayer.data.database.tables.NetworkUrl
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkUrlDao {
    @Query("SELECT * FROM networkurl ORDER BY id DESC")
    fun observeAll(): Flow<List<NetworkUrl>>

    @Query("SELECT * FROM networkurl")
    suspend fun getAll(): List<NetworkUrl>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: NetworkUrl): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<NetworkUrl>)

    @Update
    suspend fun update(item: NetworkUrl)

    @Delete
    suspend fun delete(item: NetworkUrl)

    @Query("DELETE FROM networkurl WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM networkurl")
    suspend fun clearAll()
}
