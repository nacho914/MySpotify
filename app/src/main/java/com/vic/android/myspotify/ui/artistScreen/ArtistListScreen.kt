package com.vic.android.myspotify.ui.artistScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vic.android.myspotify.domain.model.Artist

@Composable
fun ArtistListScreen(
    artists: List<Artist>,
    onArtistClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Artists",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = artists,
                key = { it.id }
            ) { artist ->
                ArtistItem(
                    artist = artist,
                    onClick = {
                        onArtistClick(artist.id)
                    }
                )
            }
        }
    }
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
        onArtistClick = {}
    )
}