package com.vic.android.myspotify.ui.albumscreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vic.android.myspotify.domain.model.Album
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun albumList_displaysAlbums() {
        val albums = listOf(
            Album(
                id = "1",
                name = "A Night at the Opera",
                imageUrl = ""
            ),
            Album(
                id = "2",
                name = "Master of Puppets",
                imageUrl = ""
            )
        )

        composeTestRule.setContent {
            AlbumListScreen(
                albums = albums,
                isLoading = false,
                error = null,
                onAlbumClick = {},
                onLoadMore = {}
            )
        }

        composeTestRule
            .onNodeWithText("A Night at the Opera")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Master of Puppets")
            .assertIsDisplayed()
    }

    @Test
    fun albumList_clickingAlbum_invokesCallback() {
        val albums = listOf(
            Album(
                id = "album-1",
                name = "A Night at the Opera",
                imageUrl = ""
            )
        )

        var clickedAlbumId: String? = null

        composeTestRule.setContent {
            AlbumListScreen(
                albums = albums,
                isLoading = false,
                error = null,
                onAlbumClick = { albumId ->
                    clickedAlbumId = albumId
                },
                onLoadMore = {}
            )
        }

        composeTestRule
            .onNode(
                hasText("A Night at the Opera") and hasClickAction()
            )
            .performClick()

        assertEquals(
            "album-1",
            clickedAlbumId
        )
    }
}