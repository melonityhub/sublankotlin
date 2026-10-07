
package com.androyal.subxplayer.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androyal.subxplayer.MainViewModel.ThemeMode
import com.androyal.subxplayer.data.repository.SettingsRepository
import com.androyal.subxplayer.utils.RevenueCatService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val hwDecoding:Boolean=true,
    val seekDuration:Int=10,
    val defaultSpeed:Float=1f,
    val resumeEnabled:Boolean=true,
    val primaryLang:String="en",
    val secondaryLang:String? = null,
    val translationTarget:String="fa",
    val catalogBaseUrl:String="https://catalog.subx.app",
    val revenueCatKey:String="goog_luBnwcCAtCYXvIihhKyRTXAmAxd",
    val subsBaseUrl:String="https://www.subx.app/subs",
    val firebaseKey:String="AIzaSyCt-RfF5oylNh4bm4MB1kjpzkJH77rPa-4",
    val isPremium:Boolean=false,
    val showSecrets:Boolean=false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repo: SettingsRepository): ViewModel(){
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    init{
        viewModelScope.launch{ repo.themeModeFlow.collect{ _state.value=_state.value.copy(themeMode=it)}}
        viewModelScope.launch{ repo.catalogBaseUrlFlow.collect{ _state.value=_state.value.copy(catalogBaseUrl=it)}}
        viewModelScope.launch{ repo.revenueCatKeyFlow.collect{ _state.value=_state.value.copy(revenueCatKey=it)}}
        viewModelScope.launch{ repo.subsBaseUrlFlow.collect{ _state.value=_state.value.copy(subsBaseUrl=it)}}
        viewModelScope.launch{ RevenueCatService.isPremium.collect{ _state.value=_state.value.copy(isPremium=it)}}
    }

    fun setHwDecoding(v:Boolean){ viewModelScope.launch{ _state.value=_state.value.copy(hwDecoding=v)}}
    fun showSecrets(){ _state.value=_state.value.copy(showSecrets=true)}
    fun hideSecrets(){ _state.value=_state.value.copy(showSecrets=false)}
    fun saveSecrets(catalog:String, rcKey:String, subs:String, firebase:String){
        viewModelScope.launch{
            repo.setCatalogBaseUrl(catalog)
            repo.setRevenueCatKey(rcKey)
            repo.setSubsBaseUrl(subs)
            repo.setFirebaseApiKey(firebase)
            try{ RevenueCatService.initWithKey(com.androyal.subxplayer.utils.ActivityHolder.currentActivity!!, rcKey)}catch(_:Exception){}
            hideSecrets()
        }
    }
}
