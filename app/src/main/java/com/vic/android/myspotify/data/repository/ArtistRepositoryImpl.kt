package com.vic.android.myspotify.data.repository

import com.vic.android.myspotify.data.remote.SpotifyApiService
import com.vic.android.myspotify.domain.model.Artist
import com.vic.android.myspotify.domain.repository.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val spotifyApiService: SpotifyApiService
) : ArtistRepository {

    override fun getArtists(): Flow<List<Artist>> {
        return flow {
            val response = spotifyApiService.searchArtists(
                query = "rock"
            )

            emit(
                response.artists.items.map { artist ->
                    Artist(
                        id = artist.id,
                        name = artist.name,
                        imageUrl = artist.images.firstOrNull()?.url.orEmpty()
                    )
                }
            )
        }
    }
}