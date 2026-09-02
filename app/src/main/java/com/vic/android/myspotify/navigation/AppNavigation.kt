package com.vic.android.myspotify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vic.android.myspotify.domain.model.Artist
import com.vic.android.myspotify.ui.artistScreen.ArtistListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Artists.route
    ) {
        composable(Screen.Artists.route) {
            ArtistListScreen(
                artists = mockArtists,
                onArtistClick = { artistId ->
                    navController.navigate(
                        Screen.Albums.createRoute(artistId)
                    )
                }
            )
        }

        composable(Screen.Albums.route) {
        }

        composable(Screen.Songs.route) {
        }
    }
}

private val mockArtists = listOf(
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