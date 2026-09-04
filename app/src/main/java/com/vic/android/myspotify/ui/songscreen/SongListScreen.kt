package com.vic.android.myspotify.ui.songscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vic.android.myspotify.domain.model.Song

@Composable
fun SongListScreen(
    songs: List<Song>,
    isLoading: Boolean,
    error: String?
) {
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        songs.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No songs found")
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = songs,
                    key = { it.id }
                ) { song ->
                    SongItem(song = song)
                }
            }
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
        songs = uiState.songs,
        isLoading = uiState.isLoading,
        error = uiState.error
    )
}