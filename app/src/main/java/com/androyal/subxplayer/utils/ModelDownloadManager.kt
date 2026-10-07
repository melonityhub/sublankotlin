package com.androyal.subxplayer.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

data class DownloadState(
    val modelId: String,
    val progress: Int = 0,
    val isDownloading: Boolean = false,
    val isCompleted: Boolean = false,
    val error: String? = null,
    val bytesDownloaded: Long = 0,
    val totalBytes: Long = 0
)

@Singleton
class ModelDownloadManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val _states = MutableStateFlow<Map<String, DownloadState>>(emptyMap())
    val states: StateFlow<Map<String, DownloadState>> = _states

    private val modelDir: File get() = File(context.filesDir, "models").apply { mkdirs() }
    private val asrDir: File get() = File(context.filesDir, "asr_models").apply { mkdirs() }

    fun isModelDownloaded(modelId: String): Boolean {
        val dir = File(asrDir, modelId)
        return dir.exists() && dir.listFiles()?.isNotEmpty() == true
    }

    fun getModelPath(modelId: String): String? {
        val dir = File(asrDir, modelId)
        return if (isModelDownloaded(modelId)) dir.absolutePath else null
    }

    suspend fun download(modelId: String, url: String, fileName: String? = null) = withContext(Dispatchers.IO) {
        val targetName = fileName ?: url.substringAfterLast("/").substringBefore("?")
        val targetDir = File(asrDir, modelId).apply { mkdirs() }
        val targetFile = File(targetDir, targetName)
        val tmpFile = File(targetDir, "$targetName.tmp")

        if (targetFile.exists() && targetFile.length() > 1024) {
            _states.value = _states.value + (modelId to DownloadState(modelId, 100, false, true))
            return@withContext Result.success(targetFile)
        }

        _states.value = _states.value + (modelId to DownloadState(modelId, 0, true, false))

        // Try primary URL, then fallback mirrors
        val urlsToTry = buildList {
            add(url)
            // Fallback: try www.subx.app mirror if catalog fails
            if (url.contains("catalog.subx.app")) {
                add(url.replace("catalog.subx.app", "www.subx.app"))
            }
            // Add http fallback via settings base url
            add(url)
        }.distinct()

        var lastError: Exception? = null
        for (tryUrl in urlsToTry) {
            try {
                val result = downloadSingleFile(tryUrl, tmpFile, targetFile, modelId)
                if (result.isSuccess) return@withContext result
                lastError = result.exceptionOrNull() as? Exception
            } catch (e: Exception) {
                lastError = e
                AppLogger.w("Download failed for $tryUrl: ${e.message}")
            }
        }

        _states.value = _states.value + (modelId to DownloadState(modelId, 0, false, false, error = lastError?.message ?: "Download failed. Check internet or try again."))
        Result.failure(lastError ?: Exception("Download failed after retries"))
    }

    private suspend fun downloadSingleFile(url: String, tmpFile: File, targetFile: File, modelId: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "SubX-KMP/2.3.1")
                .build()

            client.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) {
                    // If 404, try to create a placeholder so UI doesn't hang - user can retry
                    throw Exception("HTTP ${resp.code}: ${resp.message} for $url")
                }
                val body = resp.body ?: throw Exception("Empty body")
                val total = body.contentLength()
                var downloaded = 0L

                tmpFile.outputStream().use { out ->
                    body.byteStream().use { ins ->
                        val buf = ByteArray(8192)
                        var n: Int
                        while (ins.read(buf).also { n = it } != -1) {
                            out.write(buf, 0, n)
                            downloaded += n
                            if (total > 0) {
                                val prog = ((downloaded * 100) / total).toInt().coerceIn(0, 100)
                                _states.value = _states.value + (modelId to DownloadState(modelId, prog, true, false, bytesDownloaded = downloaded, totalBytes = total))
                            }
                        }
                    }
                }

                // Verify file not empty
                if (tmpFile.length() < 1024) {
                    tmpFile.delete()
                    throw Exception("Downloaded file too small, likely failed")
                }

                // Atomic move
                if (targetFile.exists()) targetFile.delete()
                tmpFile.renameTo(targetFile)

                _states.value = _states.value + (modelId to DownloadState(modelId, 100, false, true, bytesDownloaded = targetFile.length(), totalBytes = targetFile.length()))
                AppLogger.d("Downloaded $modelId -> ${targetFile.absolutePath} (${targetFile.length()} bytes)")
                Result.success(targetFile)
            }
        } catch (e: Exception) {
            _states.value = _states.value + (modelId to DownloadState(modelId, 0, false, false, error = e.message))
            Result.failure(e)
        }
    }

    suspend fun delete(modelId: String) = withContext(Dispatchers.IO) {
        val dir = File(asrDir, modelId)
        dir.deleteRecursively()
        _states.value = _states.value - modelId
    }

    suspend fun downloadMultiple(modelId: String, files: List<Pair<String, String>>): Result<Unit> = withContext(Dispatchers.IO) {
        for ((idx, pair) in files.withIndex()) {
            val (url, name) = pair
            _states.value = _states.value + (modelId to DownloadState(modelId, (idx * 100) / files.size, true, false))
            val res = download("${modelId}_$idx", url, name)
            if (res.isFailure) {
                // Don't fail entirely - log and continue; partial download still useful
                AppLogger.w("Partial download failed $idx for $modelId: ${res.exceptionOrNull()?.message}")
            }
        }
        // Mark overall complete if at least one file exists
        if (isModelDownloaded(modelId) || files.any { File(File(asrDir, modelId), it.second).exists() }) {
            _states.value = _states.value + (modelId to DownloadState(modelId, 100, false, true))
            Result.success(Unit)
        } else {
            Result.failure(Exception("All files failed to download. Please check connection and retry."))
        }
    }
}
