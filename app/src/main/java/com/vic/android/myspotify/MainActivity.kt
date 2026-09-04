package com.vic.android.myspotify

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.vic.android.myspotify.navigation.AppNavigation
import com.vic.android.myspotify.ui.auth.SpotifyAuthViewModel
import com.vic.android.myspotify.ui.theme.MySpotifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val spotifyAuthViewModel: SpotifyAuthViewModel by viewModels()

    private var isAuthenticated by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeAuthenticationState()

        handleAuthenticationIntent(intent)

        lifecycleScope.launch {
            val authenticated =
                spotifyAuthViewModel.initializeAuthentication()

            if (!authenticated && intent?.data == null) {
                startActivity(
                    spotifyAuthViewModel.getAuthorizationIntent()
                )
            }
        }

        setContent {
            MySpotifyTheme {
                if (isAuthenticated) {
                    AppNavigation()
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        handleAuthenticationIntent(intent)
    }

    private fun observeAuthenticationState() {
        lifecycleScope.launch {
            spotifyAuthViewModel.uiState.collectLatest { state ->
                isAuthenticated = state.isAuthenticated
            }
        }
    }

    private fun handleAuthenticationIntent(intent: Intent?) {
        val data = intent?.data ?: return

        if (
            data.scheme != "myspotify" ||
            data.host != "callback"
        ) {
            return
        }

        val code = data.getQueryParameter("code")
            ?: return

        spotifyAuthViewModel.handleAuthorizationCode(code)
    }
}