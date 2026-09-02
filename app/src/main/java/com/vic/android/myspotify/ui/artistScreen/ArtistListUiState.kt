package com.vic.android.myspotify.ui.artistScreen

import com.vic.android.myspotify.domain.model.Artist

data class ArtistListUiState(
    val artists: List<Artist> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)