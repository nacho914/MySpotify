package com.vic.android.myspotify.ui.albumscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vic.android.myspotify.domain.model.Album

@Composable
fun AlbumListScreen(
    albums: List<Album>,
    onAlbumClick: (String) -> Unit
) {
    if (albums.isEmpty()) {
        Text(text = "No albums found")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
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
        onAlbumClick = onAlbumClick
    )
}