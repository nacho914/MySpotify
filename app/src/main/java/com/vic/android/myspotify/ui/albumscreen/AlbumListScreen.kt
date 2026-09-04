package com.vic.android.myspotify.ui.albumscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vic.android.myspotify.domain.model.Album
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun AlbumListScreen(
    albums: List<Album>,
    isLoading: Boolean,
    error: String?,
    onAlbumClick: (String) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    when {
        isLoading && albums.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        error != null && albums.isEmpty() -> {
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
            LaunchedEffect(listState) {
                snapshotFlow {
                    listState.layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index
                }
                    .distinctUntilChanged()
                    .collect { lastVisibleIndex ->
                        if (
                            lastVisibleIndex != null &&
                            lastVisibleIndex >= albums.lastIndex - 2
                        ) {
                            onLoadMore()
                        }
                    }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
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
        onAlbumClick = onAlbumClick,
        onLoadMore = {
            viewModel.loadAlbums(artistId)
        }
    )
}