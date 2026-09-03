package com.vic.android.myspotify.domain.usecase

import com.vic.android.myspotify.domain.model.Artist
import com.vic.android.myspotify.domain.repository.ArtistRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetArtistsUseCase @Inject constructor(
    private val repository: ArtistRepository
) {
    operator fun invoke(): Flow<List<Artist>> {
        return repository.getArtists()
    }
}