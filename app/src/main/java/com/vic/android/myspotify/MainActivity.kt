package com.vic.android.myspotify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.vic.android.myspotify.data.auth.SpotifyAuthManager
import com.vic.android.myspotify.navigation.AppNavigation
import com.vic.android.myspotify.ui.theme.MySpotifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var spotifyAuthManager: SpotifyAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MySpotifyTheme {
                AppNavigation()
            }
        }

        if (intent?.data == null) {
            startActivity(
                spotifyAuthManager.getAuthorizationIntent()
            )
        } else {
            handleSpotifyCallback(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        handleSpotifyCallback(intent)
    }

    private fun handleSpotifyCallback(intent: Intent) {
        val data = intent.data ?: return

        if (data.scheme == "myspotify" && data.host == "callback") {
            val code = data.getQueryParameter("code") ?: return

            lifecycleScope.launch {
                try {
                    val tokenResponse =
                        spotifyAuthManager.exchangeCodeForToken(code)

                    Log.d(
                        "SpotifyAuth",
                        "Token received. Expires in: ${tokenResponse.expiresIn}"
                    )
                } catch (exception: Exception) {
                    Log.e(
                        "SpotifyAuth",
                        "Token exchange failed",
                        exception
                    )
                }
            }
        }
    }
}