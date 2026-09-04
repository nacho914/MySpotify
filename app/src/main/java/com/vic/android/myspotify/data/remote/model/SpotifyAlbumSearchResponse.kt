package com.vic.android.myspotify.data.remote.model

data class SpotifyAlbumSearchResponse(
    val items: List<SpotifyAlbumResponse>,
    val limit: Int,
    val offset: Int,
    val total: Int
)