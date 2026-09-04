package com.vic.android.myspotify.ui.artistscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vic.android.myspotify.domain.model.Artist

@Composable
fun ArtistListScreen(
    artists: List<Artist>,
    isLoading: Boolean,
    error: String?,
    onArtistClick: (String) -> Unit
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

        artists.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No artists found")
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = artists,
                    key = { it.id }
                ) { artist ->
                    ArtistItem(
                        artist = artist,
                        onArtistClick = onArtistClick
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistListRoute(
    viewModel: ArtistListViewModel,
    onArtistClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArtistListScreen(
        artists = uiState.artists,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onArtistClick = onArtistClick
    )
}

@Preview(showBackground = true)
@Composable
private fun ArtistListScreenPreview() {
    val artists = listOf(
        Artist(
            id = "1",
            name = "Coldplay",
            imageUrl = ""
        ),
        Artist(
            id = "2",
            name = "Arctic Monkeys",
            imageUrl = ""
        ),
        Artist(
            id = "3",
            name = "The Weeknd",
            imageUrl = ""
        )
    )

    ArtistListScreen(
        artists = artists,
        isLoading = false,
        error = null,
        onArtistClick = {}
    )
}