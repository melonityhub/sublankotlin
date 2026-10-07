
package com.androyal.subxplayer.utils

import android.app.Activity
import android.content.pm.ActivityInfo

object AndroidOrientationBridge {
    private var activity: Activity? = null
    fun attach(a: Activity){ activity=a }
    fun setOrientationPolicy(policy:String){
        val act = activity ?: return
        act.requestedOrientation = when(policy){
            "portrait"-> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            "landscape"-> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            "sensor"-> ActivityInfo.SCREEN_ORIENTATION_SENSOR
            else-> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}
