package com.androyal.subxplayer

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.androyal.subxplayer.ui.navigation.AppNavHost
import com.androyal.subxplayer.ui.theme.SubXTheme
import com.androyal.subxplayer.utils.AndroidOrientationBridge
import com.androyal.subxplayer.utils.AppLogger
import com.androyal.subxplayer.utils.PipManager
import com.androyal.subxplayer.utils.SharedVideoManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var pipManager: PipManager
    private lateinit var sharedVideoManager: SharedVideoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { viewModel.isLoading.value }
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        pipManager = PipManager(this)
        sharedVideoManager = SharedVideoManager(this)

        // Handle incoming intents (VIEW/SEND)
        handleIntent(intent)

        // Method channel replacements - all native bridges via ViewModel
        setupSystemChannels()

        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            val useDark = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            SubXTheme(darkTheme = useDark) {
                AppNavHost(
                    onPipEnter = { enterPipIfPossible() },
                    sharedVideoUri = viewModel.sharedVideoUri.collectAsState().value
                )
            }
        }

        lifecycleScope.launch {
            viewModel.sharedVideoUri.collect { uri ->
                uri?.let { AppLogger.d("SharedVideo received: $it") }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        val action = intent.action
        val data: Uri? = when (action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> {
                if (Build.VERSION.SDK_INT >= 33) intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                else @Suppress("DEPRECATION") intent.getParcelableExtra(Intent.EXTRA_STREAM)
            }
            else -> null
        }
        data?.let {
            Log.d("SharedVideo", "Received $action with URI: $it")
            viewModel.onSharedVideo(it)
        }
    }

    private fun setupSystemChannels() {
        AndroidOrientationBridge.attach(this)
    }

    private fun enterPipIfPossible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val params = PictureInPictureParams.Builder()
                    .setAspectRatio(Rational(16, 9))
                    .build()
                enterPictureInPictureMode(params)
            } catch (e: Exception) {
                Log.w("PIP", "enterPip failed", e)
            }
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewModel.setPipMode(isInPictureInPictureMode)
        pipManager.onPipModeChanged(isInPictureInPictureMode)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (viewModel.autoPipEnabled.value) enterPipIfPossible()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Volume handling - mirrors MainActivity volume channel
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> viewModel.onVolumeUp()
                KeyEvent.KEYCODE_VOLUME_DOWN -> viewModel.onVolumeDown()
            }
        }
        return super.dispatchKeyEvent(event)
    }

    fun isPackageInstalled(pkg: String): Boolean = try {
        packageManager.getPackageInfo(pkg, 0)
        true
    } catch (_: PackageManager.NameNotFoundException) { false }
}
