package com.vic.android.myspotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vic.android.myspotify.navigation.AppNavigation
import com.vic.android.myspotify.ui.theme.MySpotifyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MySpotifyTheme {
                AppNavigation()
            }
        }
    }
}