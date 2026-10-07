
package com.androyal.subxplayer.casting

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions
import com.google.android.gms.cast.framework.media.NotificationOptions

class CastOptionsProvider : OptionsProvider {
    override fun getCastOptions(context: Context): CastOptions {
        val notificationOptions = NotificationOptions.Builder()
            .setTargetActivityClassName("com.androyal.subxplayer.MainActivity")
            .build()
        val mediaOptions = CastMediaOptions.Builder()
            .setNotificationOptions(notificationOptions)
            .setExpandedControllerActivityClassName("com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity")
            .build()
        return CastOptions.Builder()
            .setReceiverApplicationId("ED7DABE1")
            .setCastMediaOptions(mediaOptions)
            .build()
    }
    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? = null
}
