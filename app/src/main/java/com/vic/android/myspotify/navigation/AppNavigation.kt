package com.vic.android.myspotify.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vic.android.myspotify.ui.artistscreen.ArtistListRoute
import com.vic.android.myspotify.ui.artistscreen.ArtistListViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Artists.route
    ) {
        composable(Screen.Artists.route) {
            val viewModel: ArtistListViewModel = hiltViewModel()

            ArtistListRoute(
                viewModel = viewModel,
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