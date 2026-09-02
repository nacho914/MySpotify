package com.vic.android.myspotify.navigation

sealed class Screen(
    val route: String
) {
    data object Artists : Screen("artists")

    data object Albums : Screen("albums/{artistId}") {
        fun createRoute(artistId: String): String {
            return "albums/$artistId"
        }
    }

    data object Songs : Screen("songs/{albumId}") {
        fun createRoute(albumId: String): String {
            return "songs/$albumId"
        }
    }
}