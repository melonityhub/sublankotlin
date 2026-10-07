
package com.androyal.subxplayer.casting

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.androyal.subxplayer.R

class CastForegroundService : Service() {
    override fun onCreate(){
        super.onCreate()
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val ch = NotificationChannel("cast", "Cast", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(ch)
        }
    }
    override fun onStartCommand(intent: Intent?, flags:Int, startId:Int): Int {
        val n: Notification = NotificationCompat.Builder(this,"cast")
            .setContentTitle("Casting from SubX Player")
            .setSmallIcon(R.mipmap.launcher_icon)
            .setOngoing(true)
            .build()
        startForeground(1, n)
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
