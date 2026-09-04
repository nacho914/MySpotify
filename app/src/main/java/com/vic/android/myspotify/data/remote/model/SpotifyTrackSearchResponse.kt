package com.vic.android.myspotify.data.remote.model

data class SpotifyTrackSearchResponse(
    val items: List<SpotifyTrackResponse>,
    val limit: Int,
    val offset: Int,
    val total: Int
)