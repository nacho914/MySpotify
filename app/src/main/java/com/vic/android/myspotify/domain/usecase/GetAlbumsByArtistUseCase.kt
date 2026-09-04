package com.vic.android.myspotify.domain.usecase

import com.vic.android.myspotify.domain.model.Album
import com.vic.android.myspotify.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAlbumsByArtistUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    operator fun invoke(artistId: String, offset: Int, limit: Int): Flow<List<Album>> {
        return repository.getAlbumsByArtist(
            artistId = artistId,
            offset = offset,
            limit = limit
        )
    }
}