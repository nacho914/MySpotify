package com.vic.android.myspotify.data.repository

import com.vic.android.myspotify.domain.model.Artist
import com.vic.android.myspotify.domain.repository.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor() : ArtistRepository {

    override fun getArtists(): Flow<List<Artist>> {
        return flow {
            emit(
                listOf(
                    Artist(
                        id = "1",
                        name = "Coldplay",
                        imageUrl = ""
                    ),
                    Artist(
                        id = "2",
                        name = "Arctic Monkeys",
                        imageUrl = ""
                    ),
                    Artist(
                        id = "3",
                        name = "The Weeknd",
                        imageUrl = ""
                    )
                )
            )
        }
    }
}