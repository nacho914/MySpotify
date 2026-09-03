package com.vic.android.myspotify.data.remote.model

data class SpotifyArtistSearchResponse(
    val artists: SpotifyArtistPage
)

data class SpotifyArtistPage(
    val items: List<SpotifyArtistResponse>,
    val limit: Int,
    val offset: Int,
    val total: Int
)