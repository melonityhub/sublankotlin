package com.androyal.subxplayer.utils

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Stubbed - all premium unlocked. No billing dependency.
 * Keeps API compatible so UI code doesn't break.
 */
object RevenueCatService {
    private val _isPremium = MutableStateFlow(true)
    val isPremium: StateFlow<Boolean> = _isPremium
    private val _isInitialized = MutableStateFlow(true)

    fun init(context: Context, apiKey: String = "") {
        _isPremium.value = true
        _isInitialized.value = true
    }

    fun initWithKey(context: Context, key: String) {
        _isPremium.value = true
        _isInitialized.value = true
    }

    fun refresh() {
        _isPremium.value = true
    }

    fun purchase(packageToBuy: Any, onResult: (Boolean, String?) -> Unit) {
        // All features are free - instantly succeed
        onResult(true, null)
        _isPremium.value = true
    }

    fun restore(onDone: (Boolean) -> Unit) {
        _isPremium.value = true
        onDone(true)
    }
}
