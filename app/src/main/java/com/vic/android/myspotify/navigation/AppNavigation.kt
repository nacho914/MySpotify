package com.vic.android.myspotify.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vic.android.myspotify.ui.albumscreen.AlbumListRoute
import com.vic.android.myspotify.ui.albumscreen.AlbumListViewModel
import com.vic.android.myspotify.ui.artistscreen.ArtistListRoute
import com.vic.android.myspotify.ui.artistscreen.ArtistListViewModel
import com.vic.android.myspotify.ui.songscreen.SongListRoute
import com.vic.android.myspotify.ui.songscreen.SongListViewModel

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

        composable(
            route = Screen.Albums.route,
            arguments = listOf(
                navArgument("artistId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val artistId =
                backStackEntry.arguments?.getString("artistId")
                    ?: return@composable

            val viewModel: AlbumListViewModel = hiltViewModel()

            AlbumListRoute(
                artistId = artistId,
                viewModel = viewModel,
                onAlbumClick = { albumId ->
                    navController.navigate(
                        Screen.Songs.createRoute(albumId)
                    )
                }
            )
        }

        composable(
            route = Screen.Songs.route,
            arguments = listOf(
                navArgument("albumId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val albumId =
                backStackEntry.arguments?.getString("albumId")
                    ?: return@composable

            val viewModel: SongListViewModel = hiltViewModel()

            SongListRoute(
                albumId = albumId,
                viewModel = viewModel
            )
        }
    }
}