package com.vic.android.myspotify.data.repository

import com.vic.android.myspotify.data.remote.SpotifyApiService
import com.vic.android.myspotify.domain.model.Album
import com.vic.android.myspotify.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val spotifyApiService: SpotifyApiService
) : AlbumRepository {

    override fun getAlbumsByArtist(
        artistId: String,
        offset: Int,
        limit: Int
    ): Flow<List<Album>> {
        return flow {
            val response = spotifyApiService.getArtistAlbums(
                artistId = artistId,
                offset = offset,
                limit = limit
            )

            emit(
                response.items.map { album ->
                    Album(
                        id = album.id,
                        name = album.name,
                        imageUrl = album.images
                            .firstOrNull()
                            ?.url
                            .orEmpty()
                    )
                }
            )
        }
    }
}