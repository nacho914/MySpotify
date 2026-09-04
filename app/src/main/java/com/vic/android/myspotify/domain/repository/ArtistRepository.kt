package com.vic.android.myspotify.domain.repository

import com.vic.android.myspotify.domain.model.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun getArtists(offset: Int, limit: Int): Flow<List<Artist>>
}