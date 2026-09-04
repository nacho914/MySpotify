package com.vic.android.myspotify.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vic.android.myspotify.data.auth.SpotifyAuthManager
import com.vic.android.myspotify.data.auth.SpotifyAuthStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyAuthViewModel @Inject constructor(
    private val spotifyAuthManager: SpotifyAuthManager,
    private val spotifyAuthStorage: SpotifyAuthStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SpotifyAuthUiState()
    )
    private var authenticationInitialized = false
    val uiState: StateFlow<SpotifyAuthUiState> =
        _uiState.asStateFlow()

    suspend fun initializeAuthentication(): Boolean {

        if (authenticationInitialized) {
            return _uiState.value.isAuthenticated
        }

        authenticationInitialized = true

        if (spotifyAuthStorage.isAccessTokenValid()) {
            _uiState.value = SpotifyAuthUiState(
                isAuthenticated = true
            )
            return true
        }

        if (!spotifyAuthStorage
                .getRefreshToken()
                .isNullOrBlank()
        ) {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            return try {
                spotifyAuthManager.refreshAccessToken()

                _uiState.value = SpotifyAuthUiState(
                    isAuthenticated = true
                )

                true
            } catch (exception: Exception) {
                spotifyAuthStorage.clearTokens()

                _uiState.value = SpotifyAuthUiState(
                    isAuthenticated = false
                )

                false
            }
        }

        _uiState.value = SpotifyAuthUiState(
            isAuthenticated = false
        )

        return false
    }

    fun getAuthorizationIntent() =
        spotifyAuthManager.getAuthorizationIntent()

    fun handleAuthorizationCode(code: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                spotifyAuthManager.exchangeCodeForToken(code)

                _uiState.value = SpotifyAuthUiState(
                    isAuthenticated = true
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message
                )
            }
        }
    }
}