package com.vic.android.myspotify.ui.albumscreen

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
import com.vic.android.myspotify.domain.model.Album

@Composable
fun AlbumListScreen(
    albums: List<Album>,
    isLoading: Boolean,
    error: String?,
    onAlbumClick: (String) -> Unit
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

        albums.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No albums found")
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = albums,
                    key = { it.id }
                ) { album ->
                    AlbumItem(
                        album = album,
                        onAlbumClick = onAlbumClick
                    )
                }
            }
        }
    }
}

@Composable
fun AlbumListRoute(
    artistId: String,
    viewModel: AlbumListViewModel,
    onAlbumClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(artistId) {
        viewModel.loadAlbums(artistId)
    }

    AlbumListScreen(
        albums = uiState.albums,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onAlbumClick = onAlbumClick
    )
}