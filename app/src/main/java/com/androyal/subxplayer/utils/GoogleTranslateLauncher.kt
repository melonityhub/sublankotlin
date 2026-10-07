
package com.androyal.subxplayer.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

object GoogleTranslateLauncher {
    fun launchProcessText(context: Context, text:String, targetPackage:String="com.google.android.apps.translate"): Boolean {
        return try{
            val intent = Intent().apply{
                action="android.intent.action.PROCESS_TEXT"
                type="text/plain"
                putExtra("android.intent.extra.PROCESS_TEXT", text)
                putExtra("android.intent.extra.PROCESS_TEXT_READONLY", true)
                setPackage(targetPackage)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if(context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)==null) return false
            context.startActivity(intent)
            true
        }catch(_:Exception){ false }
    }
}
