
package com.androyal.subxplayer.casting

import android.content.Context
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CastServiceController @Inject constructor(@ApplicationContext private val context: Context) {
    private val _isCasting = MutableStateFlow(false)
    val isCasting: StateFlow<Boolean> = _isCasting

    fun isConnected(): Boolean = try{
        CastContext.getSharedInstance(context).sessionManager.currentCastSession?.isConnected == true
    }catch(_:Exception){ false }

    fun castVideo(url:String, title:String, subsUrl:String? = null){
        // Build MediaInfo and load via RemoteMediaClient
    }
}
