package com.vic.android.myspotify.ui.albumscreen

import com.vic.android.myspotify.domain.model.Album

data class AlbumListUiState(
    val albums: List<Album> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)