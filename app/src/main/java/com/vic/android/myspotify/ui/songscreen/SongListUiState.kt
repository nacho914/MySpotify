package com.vic.android.myspotify.ui.songscreen

import com.vic.android.myspotify.domain.model.Song

data class SongListUiState(
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)