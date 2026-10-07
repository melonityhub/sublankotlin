package com.androyal.subxplayer.data.database.daos
import androidx.room.*
import com.androyal.subxplayer.data.database.tables.FavouriteFolder
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteFolderDao {
    @Query("SELECT * FROM favouritefolder ORDER BY id DESC")
    fun observeAll(): Flow<List<FavouriteFolder>>

    @Query("SELECT * FROM favouritefolder")
    suspend fun getAll(): List<FavouriteFolder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavouriteFolder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FavouriteFolder>)

    @Update
    suspend fun update(item: FavouriteFolder)

    @Delete
    suspend fun delete(item: FavouriteFolder)

    @Query("DELETE FROM favouritefolder WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM favouritefolder")
    suspend fun clearAll()
}
