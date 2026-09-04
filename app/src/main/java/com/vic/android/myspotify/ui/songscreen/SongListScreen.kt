package com.vic.android.myspotify.ui.songscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vic.android.myspotify.domain.model.Song

@Composable
fun SongListScreen(
    songs: List<Song>
) {
    if (songs.isEmpty()) {
        Text(text = "No songs found")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = songs,
            key = { it.id }
        ) { song ->
            SongItem(song = song)
        }
    }
}

@Composable
fun SongListRoute(
    albumId: String,
    viewModel: SongListViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(albumId) {
        viewModel.loadSongs(albumId)
    }

    SongListScreen(
        songs = uiState.songs
    )
}