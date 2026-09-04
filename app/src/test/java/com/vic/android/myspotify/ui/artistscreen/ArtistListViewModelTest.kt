package com.vic.android.myspotify.ui.artistscreen

import com.vic.android.myspotify.domain.model.Artist
import com.vic.android.myspotify.domain.usecase.GetArtistsUseCase
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
class ArtistListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getArtistsUseCase: GetArtistsUseCase
    private lateinit var viewModel: ArtistListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getArtistsUseCase = mockk()
        viewModel = ArtistListViewModel(
            getArtistsUseCase = getArtistsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadArtists updates state with artists`() = runTest {
        val artists = listOf(
            Artist(
                id = "1",
                name = "Queen",
                imageUrl = "image1"
            ),
            Artist(
                id = "2",
                name = "Metallica",
                imageUrl = "image2"
            )
        )

        every {
            getArtistsUseCase(
                offset = 0,
                limit = 10
            )
        } returns flowOf(artists)

        viewModel.loadArtists()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(artists, state.artists)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadArtists appends next page to existing artists`() = runTest {
        val firstPage = listOf(
            Artist(
                id = "1",
                name = "Queen",
                imageUrl = "image1"
            ),
            Artist(
                id = "2",
                name = "Metallica",
                imageUrl = "image2"
            )
        )

        val secondPage = listOf(
            Artist(
                id = "3",
                name = "Nirvana",
                imageUrl = "image3"
            ),
            Artist(
                id = "4",
                name = "Pearl Jam",
                imageUrl = "image4"
            )
        )

        every {
            getArtistsUseCase(
                offset = 0,
                limit = 10
            )
        } returns flowOf(firstPage)

        every {
            getArtistsUseCase(
                offset = 2,
                limit = 10
            )
        } returns flowOf(secondPage)

        viewModel.loadArtists()

        advanceUntilIdle()

        viewModel.loadArtists()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(
            firstPage + secondPage,
            state.artists
        )

        assertEquals(4, state.artists.size)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadArtists ignores request while loading`() = runTest {
        val firstPage = listOf(
            Artist(
                id = "1",
                name = "Queen",
                imageUrl = "image1"
            )
        )

        val requestStarted = kotlinx.coroutines.CompletableDeferred<Unit>()
        val continueRequest = kotlinx.coroutines.CompletableDeferred<Unit>()

        every {
            getArtistsUseCase(
                offset = 0,
                limit = 10
            )
        } returns kotlinx.coroutines.flow.flow {
            requestStarted.complete(Unit)
            continueRequest.await()
            emit(firstPage)
        }

        viewModel.loadArtists()

        requestStarted.await()

        viewModel.loadArtists()

        io.mockk.verify(exactly = 1) {
            getArtistsUseCase(
                offset = 0,
                limit = 10
            )
        }

        continueRequest.complete(Unit)

        advanceUntilIdle()

        assertEquals(
            firstPage,
            viewModel.uiState.value.artists
        )
    }

    @Test
    fun `loadArtists updates error when request fails`() = runTest {
        val exception = RuntimeException("Network error")

        every {
            getArtistsUseCase(
                offset = 0,
                limit = 10
            )
        } returns kotlinx.coroutines.flow.flow {
            throw exception
        }

        viewModel.loadArtists()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.artists.isEmpty())
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
    }
}