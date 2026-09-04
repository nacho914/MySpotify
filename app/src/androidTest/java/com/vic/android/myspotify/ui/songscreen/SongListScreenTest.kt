package com.vic.android.myspotify.ui.songscreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vic.android.myspotify.domain.model.Song
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun songList_displaysSongs() {
        val songs = listOf(
            Song(
                id = "1",
                name = "Bohemian Rhapsody",
                durationMs = 355000
            ),
            Song(
                id = "2",
                name = "Enter Sandman",
                durationMs = 331000
            )
        )

        composeTestRule.setContent {
            SongListScreen(
                songs = songs,
                isLoading = false,
                error = null,
                onLoadMore = {}
            )
        }

        composeTestRule
            .onNodeWithText("Bohemian Rhapsody")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Enter Sandman")
            .assertIsDisplayed()
    }

    @Test
    fun songList_displaysFormattedDuration() {
        val songs = listOf(
            Song(
                id = "1",
                name = "Bohemian Rhapsody",
                durationMs = 355000
            )
        )

        composeTestRule.setContent {
            SongListScreen(
                songs = songs,
                isLoading = false,
                error = null,
                onLoadMore = {}
            )
        }

        composeTestRule
            .onNodeWithText("5:55")
            .assertIsDisplayed()
    }
}