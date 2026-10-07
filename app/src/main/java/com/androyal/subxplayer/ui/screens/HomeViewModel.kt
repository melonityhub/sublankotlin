
package com.androyal.subxplayer.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androyal.subxplayer.data.database.daos.MediaLibraryIndexDao
import com.androyal.subxplayer.data.models.FolderEntry
import com.androyal.subxplayer.data.models.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class HomeUiState(
    val isLoading:Boolean=false,
    val videos:List<VideoItem> = emptyList(),
    val folders:List<FolderEntry> = emptyList(),
    val breadcrumb:List<String> = emptyList(),
    val searchQuery:String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mediaDao: MediaLibraryIndexDao
): ViewModel(){
    private val _uiState = MutableStateFlow(HomeUiState(isLoading=true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init { loadMedia() }

    fun loadMedia(path:String? = null){
        viewModelScope.launch(Dispatchers.IO){
            _uiState.value = _uiState.value.copy(isLoading=true)
            val videos = queryVideos(path)
            val folders = groupFolders(videos)
            _uiState.value = HomeUiState(isLoading=false, videos=videos, folders=folders, breadcrumb= path?.split("/")?.filter{it.isNotEmpty()}?: emptyList())
        }
    }

    private suspend fun queryVideos(folderPath:String?): List<VideoItem> = withContext(Dispatchers.IO){
        val list = mutableListOf<VideoItem>()
        try{
            val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val proj = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DATE_ADDED
            )
            val sel = if(folderPath!=null) "${MediaStore.Video.Media.DATA} LIKE ?" else null
            val args = if(folderPath!=null) arrayOf("$folderPath%") else null
            context.contentResolver.query(uri, proj, sel, args, "${MediaStore.Video.Media.DATE_ADDED} DESC")?.use{ c ->
                val idIdx = c.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameIdx = c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val durIdx = c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val sizeIdx = c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val wIdx = c.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
                val hIdx = c.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
                val dataIdx = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val mimeIdx = c.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
                val dateIdx = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
                while(c.moveToNext()){
                    val id = c.getLong(idIdx)
                    val contentUri = Uri.withAppendedPath(uri, id.toString()).toString()
                    list.add(VideoItem(
                        id=id.toString(),
                        uri=contentUri,
                        displayName=c.getString(nameIdx) ?: "video",
                        durationMs=c.getLong(durIdx),
                        sizeBytes=c.getLong(sizeIdx),
                        width=c.getInt(wIdx),
                        height=c.getInt(hIdx),
                        mimeType=c.getString(mimeIdx),
                        folderPath=c.getString(dataIdx)?.substringBeforeLast("/") ?: "",
                        dateAdded=c.getLong(dateIdx)
                    ))
                }
            }
        }catch(_:Exception){}
        list
    }

    private fun groupFolders(videos:List<VideoItem>): List<FolderEntry>{
        return videos.groupBy{ it.folderPath }.map{ (path, vs) ->
            FolderEntry(path=path, name=path.substringAfterLast("/").ifEmpty{"Root"}, videoCount=vs.size, totalDurationMs=vs.sumOf{it.durationMs})
        }.sortedBy{ it.name }
    }

    fun openFolder(entry: FolderEntry){ loadMedia(entry.path) }
    fun playFolder(entry: FolderEntry){ /* delegate to PlaybackManager */ }
    fun navigateToBreadcrumb(index:Int){
        val path = _uiState.value.breadcrumb.take(index+1).joinToString("/", prefix="/")
        loadMedia(if(index<0) null else path)
    }
    fun onSearch(q:String){ _uiState.value = _uiState.value.copy(searchQuery=q) }
    fun pickMedia(){ /* file picker */ }
}
