
package com.androyal.subxplayer.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androyal.subxplayer.data.database.daos.*
import com.androyal.subxplayer.data.database.tables.*

@Database(
    entities = [
        FavouriteFolder::class,
        MediaLibraryIndex::class,
        NetworkUrl::class,
        NewLibraryMedia::class,
        PlaybackState::class,
        SubtitleBookmark::class,
        TranslationCache::class,
        VideoCache::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteFolderDao(): FavouriteFolderDao
    abstract fun mediaLibraryIndexDao(): MediaLibraryIndexDao
    abstract fun networkUrlDao(): NetworkUrlDao
    abstract fun newLibraryMediaDao(): NewLibraryMediaDao
    abstract fun playbackStateDao(): PlaybackStateDao
    abstract fun subtitleBookmarkDao(): SubtitleBookmarkDao
    abstract fun translationCacheDao(): TranslationCacheDao
    abstract fun videoCacheDao(): VideoCacheDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun init(context: Context) { get(context) }
        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "subx.db")
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}
