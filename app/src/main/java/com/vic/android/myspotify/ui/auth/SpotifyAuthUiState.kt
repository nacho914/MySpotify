package com.vic.android.myspotify.ui.auth

data class SpotifyAuthUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)