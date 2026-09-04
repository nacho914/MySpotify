package com.vic.android.myspotify.data.remote

import com.vic.android.myspotify.data.remote.model.SpotifyAlbumSearchResponse
import com.vic.android.myspotify.data.remote.model.SpotifyArtistSearchResponse
import com.vic.android.myspotify.data.remote.model.SpotifyTrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApiService {

    @GET("v1/search")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("type") type: String = "artist",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): SpotifyArtistSearchResponse

    @GET("v1/artists/{artistId}/albums")
    suspend fun getArtistAlbums(
        @Path("artistId") artistId: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): SpotifyAlbumSearchResponse

    @GET("v1/albums/{albumId}/tracks")
    suspend fun getAlbumTracks(
        @Path("albumId") albumId: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): SpotifyTrackSearchResponse
}