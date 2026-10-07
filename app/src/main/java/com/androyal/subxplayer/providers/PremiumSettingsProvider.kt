package com.androyal.subxplayer.providers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PremiumSettingsProvider @Inject constructor() {
    private val _state = MutableStateFlow(Any())
    val state: StateFlow<Any> = _state
}
