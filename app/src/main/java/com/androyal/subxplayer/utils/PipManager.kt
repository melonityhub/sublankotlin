
package com.androyal.subxplayer.utils

import android.app.Activity
import android.app.PictureInPictureParams
import android.os.Build
import android.util.Rational

class PipManager(private val activity: Activity) {
    var isInPip: Boolean = false
        private set
    fun onPipModeChanged(enabled:Boolean){ isInPip = enabled }
    fun enterPip(aspect: Rational = Rational(16,9)){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val p = PictureInPictureParams.Builder().setAspectRatio(aspect).build()
                activity.enterPictureInPictureMode(p)
            } catch(_:Exception){}
        }
    }
}
