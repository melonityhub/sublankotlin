
package com.androyal.subxplayer.di

import android.content.Context
import com.androyal.subxplayer.data.database.AppDatabase
import com.androyal.subxplayer.data.database.daos.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun provideDb(@ApplicationContext c: Context): AppDatabase = AppDatabase.get(c)
    @Provides fun providePlaybackDao(db: AppDatabase): PlaybackStateDao = db.playbackStateDao()
    @Provides fun provideTranslationCacheDao(db: AppDatabase): TranslationCacheDao = db.translationCacheDao()
    @Provides fun provideMediaLibraryDao(db: AppDatabase): MediaLibraryIndexDao = db.mediaLibraryIndexDao()
    @Provides fun provideFavouriteFolderDao(db: AppDatabase): FavouriteFolderDao = db.favouriteFolderDao()
    @Provides fun provideNetworkUrlDao(db: AppDatabase): NetworkUrlDao = db.networkUrlDao()
    @Provides fun provideSubtitleBookmarkDao(db: AppDatabase): SubtitleBookmarkDao = db.subtitleBookmarkDao()
    @Provides fun provideVideoCacheDao(db: AppDatabase): VideoCacheDao = db.videoCacheDao()
    @Provides fun provideNewLibraryMediaDao(db: AppDatabase): NewLibraryMediaDao = db.newLibraryMediaDao()
}
