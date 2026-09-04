package com.vic.android.myspotify.ui.albumscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.vic.android.myspotify.domain.model.Album

@Composable
fun AlbumItem(
    album: Album,
    onAlbumClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.clickable {
            onAlbumClick(album.id)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = album.imageUrl,
            contentDescription = album.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = album.name)
    }
}