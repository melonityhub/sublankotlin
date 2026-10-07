
package com.androyal.subxplayer.utils

import android.content.Context
import android.util.Log
import java.io.File

object AppLogger {
    private var logDir: File? = null
    fun init(context: Context){ logDir = File(context.filesDir, "logs").apply{ mkdirs() } }
    fun d(msg:String){ Log.d("SubX", msg) }
    fun i(msg:String){ Log.i("SubX", msg) }
    fun w(msg:String, e:Throwable? = null){ Log.w("SubX", msg, e) }
    fun e(msg:String, e:Throwable? = null){ Log.e("SubX", msg, e) }
}
