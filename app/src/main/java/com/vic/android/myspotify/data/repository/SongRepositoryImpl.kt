package com.vic.android.myspotify.data.repository

import com.vic.android.myspotify.data.remote.SpotifyApiService
import com.vic.android.myspotify.domain.model.Song
import com.vic.android.myspotify.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val spotifyApiService: SpotifyApiService
) : SongRepository {

    override fun getSongsByAlbum(
        albumId: String,
        offset: Int,
        limit: Int
    ): Flow<List<Song>> {
        return flow {
            val response = spotifyApiService.getAlbumTracks(
                albumId = albumId,
                offset = offset,
                limit = limit
            )

            emit(
                response.items.map { track ->
                    Song(
                        id = track.id,
                        name = track.name,
                        durationMs = track.duration_ms
                    )
                }
            )
        }
    }
}