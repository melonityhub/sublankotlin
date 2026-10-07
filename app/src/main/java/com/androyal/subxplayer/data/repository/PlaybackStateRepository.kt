
package com.androyal.subxplayer.data.repository

import com.androyal.subxplayer.data.database.daos.PlaybackStateDao
import com.androyal.subxplayer.data.database.tables.PlaybackState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackStateRepository @Inject constructor(private val dao: PlaybackStateDao) {
    fun observeAll(): Flow<List<PlaybackState>> = dao.observeAll()
    suspend fun save(videoId: String, positionMs: Long, durationMs: Long, speed: Float=1f){
        dao.insert(PlaybackState(id=0, videoId=videoId, positionMs=positionMs, durationMs=durationMs, lastPlayedAt=System.currentTimeMillis(), playbackSpeed=speed))
    }
    suspend fun getLastPosition(videoId: String): Long {
        return dao.getAll().firstOrNull{it.videoId==videoId}?.positionMs ?: 0L
    }
}
