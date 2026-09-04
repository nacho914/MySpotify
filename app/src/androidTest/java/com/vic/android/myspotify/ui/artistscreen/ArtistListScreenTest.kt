package com.vic.android.myspotify.ui.artistscreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vic.android.myspotify.domain.model.Artist
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun artistList_displaysArtists() {
        val artists = listOf(
            Artist(
                id = "1",
                name = "Queen",
                imageUrl = ""
            ),
            Artist(
                id = "2",
                name = "Metallica",
                imageUrl = ""
            )
        )

        composeTestRule.setContent {
            ArtistListScreen(
                artists = artists,
                isLoading = false,
                error = null,
                onArtistClick = {},
                onLoadMore = {}
            )
        }

        composeTestRule
            .onNodeWithText("Queen")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Metallica")
            .assertIsDisplayed()
    }

    @Test
    fun artistList_clickingArtist_invokesCallback() {
        val artists = listOf(
            Artist(
                id = "artist-1",
                name = "Queen",
                imageUrl = ""
            )
        )

        var clickedArtistId: String? = null

        composeTestRule.setContent {
            ArtistListScreen(
                artists = artists,
                isLoading = false,
                error = null,
                onArtistClick = { artistId ->
                    clickedArtistId = artistId
                },
                onLoadMore = {}
            )
        }

        composeTestRule
            .onNode(
                hasText("Queen") and hasClickAction()
            )
            .performClick()

        assertEquals(
            "artist-1",
            clickedArtistId
        )
    }
}