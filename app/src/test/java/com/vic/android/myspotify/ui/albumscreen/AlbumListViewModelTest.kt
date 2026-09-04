package com.vic.android.myspotify.ui.albumscreen

import com.vic.android.myspotify.domain.model.Album
import com.vic.android.myspotify.domain.usecase.GetAlbumsByArtistUseCase
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getAlbumsByArtistUseCase: GetAlbumsByArtistUseCase
    private lateinit var viewModel: AlbumListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getAlbumsByArtistUseCase = mockk()

        viewModel = AlbumListViewModel(
            getAlbumsByArtistUseCase = getAlbumsByArtistUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAlbums updates state with albums`() = runTest {
        val albums = listOf(
            Album(
                id = "1",
                name = "Album One",
                imageUrl = "image1"
            ),
            Album(
                id = "2",
                name = "Album Two",
                imageUrl = "image2"
            )
        )

        every {
            getAlbumsByArtistUseCase(
                artistId = "artist-1",
                offset = 0,
                limit = 10
            )
        } returns flowOf(albums)

        viewModel.loadAlbums("artist-1")

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(albums, state.albums)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadAlbums appends next page to existing albums`() = runTest {
        val firstPage = listOf(
            Album(
                id = "1",
                name = "Album One",
                imageUrl = "image1"
            ),
            Album(
                id = "2",
                name = "Album Two",
                imageUrl = "image2"
            )
        )

        val secondPage = listOf(
            Album(
                id = "3",
                name = "Album Three",
                imageUrl = "image3"
            ),
            Album(
                id = "4",
                name = "Album Four",
                imageUrl = "image4"
            )
        )

        every {
            getAlbumsByArtistUseCase(
                artistId = "artist-1",
                offset = 0,
                limit = 10
            )
        } returns flowOf(firstPage)

        every {
            getAlbumsByArtistUseCase(
                artistId = "artist-1",
                offset = 2,
                limit = 10
            )
        } returns flowOf(secondPage)

        viewModel.loadAlbums("artist-1")

        advanceUntilIdle()

        viewModel.loadAlbums("artist-1")

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(
            firstPage + secondPage,
            state.albums
        )

        assertEquals(4, state.albums.size)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadAlbums ignores request while loading`() = runTest {
        val albums = listOf(
            Album(
                id = "1",
                name = "Album One",
                imageUrl = "image1"
            )
        )

        val requestStarted = kotlinx.coroutines.CompletableDeferred<Unit>()
        val continueRequest = kotlinx.coroutines.CompletableDeferred<Unit>()

        every {
            getAlbumsByArtistUseCase(
                artistId = "artist-1",
                offset = 0,
                limit = 10
            )
        } returns kotlinx.coroutines.flow.flow {
            requestStarted.complete(Unit)
            continueRequest.await()
            emit(albums)
        }

        viewModel.loadAlbums("artist-1")

        requestStarted.await()

        viewModel.loadAlbums("artist-1")

        io.mockk.verify(exactly = 1) {
            getAlbumsByArtistUseCase(
                artistId = "artist-1",
                offset = 0,
                limit = 10
            )
        }

        continueRequest.complete(Unit)

        advanceUntilIdle()

        assertEquals(
            albums,
            viewModel.uiState.value.albums
        )
    }

    @Test
    fun `loadAlbums updates error when request fails`() = runTest {
        val exception = RuntimeException("Network error")

        every {
            getAlbumsByArtistUseCase(
                artistId = "artist-1",
                offset = 0,
                limit = 10
            )
        } returns kotlinx.coroutines.flow.flow {
            throw exception
        }

        viewModel.loadAlbums("artist-1")

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.albums.isEmpty())
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
    }
}