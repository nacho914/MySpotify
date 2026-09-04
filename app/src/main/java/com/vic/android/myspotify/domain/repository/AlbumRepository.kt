package com.vic.android.myspotify.domain.repository

import com.vic.android.myspotify.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun getAlbumsByArtist(artistId: String): Flow<List<Album>>
}