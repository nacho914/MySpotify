package com.vic.android.myspotify.domain.usecase

import com.vic.android.myspotify.domain.model.Album
import com.vic.android.myspotify.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsByArtistUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    operator fun invoke(artistId: String): Flow<List<Album>> {
        return repository.getAlbumsByArtist(artistId)
    }
}