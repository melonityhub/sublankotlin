package com.androyal.subxplayer

import android.app.Application
import com.androyal.subxplayer.data.database.AppDatabase
import com.androyal.subxplayer.utils.AppLogger
import com.androyal.subxplayer.utils.RevenueCatService
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class SubxApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
        AppLogger.init(this)
        AppDatabase.init(this)

        applicationScope.launch {
            try {
                RevenueCatService.init(this@SubxApplication)
            } catch (e: Exception) {
                AppLogger.w("RevenueCat init failed", e)
            }
        }
    }
}
