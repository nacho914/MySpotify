package com.vic.android.myspotify.domain.repository

import com.vic.android.myspotify.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun getSongsByAlbum(albumId: String, offset: Int, limit: Int): Flow<List<Song>>
}