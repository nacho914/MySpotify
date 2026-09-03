package com.vic.android.myspotify.data.remote.model

data class SpotifyArtistResponse(
    val id: String,
    val name: String,
    val images: List<SpotifyImageResponse>
)