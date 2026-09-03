package com.vic.android.myspotify.data.remote

import com.vic.android.myspotify.data.remote.model.SpotifyArtistSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyApiService {

    @GET("v1/search")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("type") type: String = "artist",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): SpotifyArtistSearchResponse
}