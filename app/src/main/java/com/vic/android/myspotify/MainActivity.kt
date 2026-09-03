package com.vic.android.myspotify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.vic.android.myspotify.data.auth.SpotifyAuthManager
import com.vic.android.myspotify.data.auth.SpotifyAuthStorage
import com.vic.android.myspotify.navigation.AppNavigation
import com.vic.android.myspotify.ui.theme.MySpotifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var spotifyAuthManager: SpotifyAuthManager

    @Inject
    lateinit var spotifyAuthStorage: SpotifyAuthStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleAuthenticationIntent(intent)

        setContent {
            MySpotifyTheme {
                AppNavigation()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        handleAuthenticationIntent(intent)
    }

    private fun handleAuthenticationIntent(intent: Intent?) {
        val callbackData = intent?.data

        if (callbackData != null) {
            handleSpotifyCallback(intent)
            return
        }

        if (spotifyAuthStorage.getAccessToken().isNullOrBlank()) {
            startActivity(
                spotifyAuthManager.getAuthorizationIntent()
            )
        }
    }

    private fun handleSpotifyCallback(intent: Intent) {
        val data = intent.data ?: return

        if (data.scheme != "myspotify" || data.host != "callback") {
            return
        }

        val code = data.getQueryParameter("code")

        if (code.isNullOrBlank()) {
            Log.e(
                "SpotifyAuth",
                "Authorization code not found"
            )
            return
        }

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