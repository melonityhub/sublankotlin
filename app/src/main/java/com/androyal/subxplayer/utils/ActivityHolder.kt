
package com.androyal.subxplayer.utils

import android.app.Activity
import java.lang.ref.WeakReference

object ActivityHolder {
    private var ref: WeakReference<Activity>? = null
    var currentActivity: Activity?
        get() = ref?.get()
        set(v){ ref = if(v==null) null else WeakReference(v) }
}
