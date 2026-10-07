package com.androyal.subxplayer

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androyal.subxplayer.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ThemeMode { LIGHT, DARK, SYSTEM }

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _sharedVideoUri = MutableStateFlow<Uri?>(null)
    val sharedVideoUri: StateFlow<Uri?> = _sharedVideoUri.asStateFlow()

    private val _isPipMode = MutableStateFlow(false)
    val isPipMode: StateFlow<Boolean> = _isPipMode.asStateFlow()

    private val _autoPipEnabled = MutableStateFlow(true)
    val autoPipEnabled: StateFlow<Boolean> = _autoPipEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.themeModeFlow.collect { _themeMode.value = it }
        }
        viewModelScope.launch {
            settingsRepository.autoPipFlow.collect { _autoPipEnabled.value = it }
        }
        // splash delay simulation + preload
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _isLoading.value = false
        }
    }

    fun onSharedVideo(uri: Uri) { _sharedVideoUri.value = uri }
    fun consumeSharedVideo() { _sharedVideoUri.value = null }
    fun setPipMode(enabled: Boolean) { _isPipMode.value = enabled }
    fun onVolumeUp() {}
    fun onVolumeDown() {}
}
