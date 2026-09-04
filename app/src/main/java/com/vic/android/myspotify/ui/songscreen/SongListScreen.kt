package com.vic.android.myspotify.ui.songscreen

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
import com.vic.android.myspotify.domain.model.Song
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun SongListScreen(
    songs: List<Song>,
    isLoading: Boolean,
    error: String?,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    when {
        isLoading && songs.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        error != null && songs.isEmpty() -> {
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
                            lastVisibleIndex >= songs.lastIndex - 2
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
        error = uiState.error,
        onLoadMore = {
            viewModel.loadSongs(albumId)
        }
    )
}