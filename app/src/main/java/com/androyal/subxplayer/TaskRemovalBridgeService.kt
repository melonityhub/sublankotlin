package com.androyal.subxplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.androyal.subxplayer.utils.AppLogger

class TaskRemovalBridgeService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onTaskRemoved(rootIntent: Intent?) {
        AppLogger.d("TaskRemovalBridge: onTaskRemoved")
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }
}
