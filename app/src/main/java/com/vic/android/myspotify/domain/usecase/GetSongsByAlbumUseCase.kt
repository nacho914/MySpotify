package com.vic.android.myspotify.domain.usecase

import com.vic.android.myspotify.domain.model.Song
import com.vic.android.myspotify.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetSongsByAlbumUseCase @Inject constructor(
    private val repository: SongRepository
) {
    operator fun invoke(albumId: String): Flow<List<Song>> {
        return repository.getSongsByAlbum(albumId)
    }
}