package com.androyal.subxplayer

import android.app.Application
import com.androyal.subxplayer.data.database.AppDatabase
import com.androyal.subxplayer.utils.AppLogger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class SubxApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        AppLogger.init(this)
        AppDatabase.init(this)
        // All premium features are unlocked - no RevenueCat / Firebase needed
        AppLogger.d("SubX KMP started - all features unlocked")
    }
}
