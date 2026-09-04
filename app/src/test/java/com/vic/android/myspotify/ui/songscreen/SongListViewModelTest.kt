package com.vic.android.myspotify.ui.songscreen

import com.vic.android.myspotify.domain.model.Song
import com.vic.android.myspotify.domain.usecase.GetSongsByAlbumUseCase
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
class SongListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getSongsByAlbumUseCase: GetSongsByAlbumUseCase
    private lateinit var viewModel: SongListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getSongsByAlbumUseCase = mockk()

        viewModel = SongListViewModel(
            getSongsByAlbumUseCase = getSongsByAlbumUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSongs updates state with songs`() = runTest {
        val songs = listOf(
            Song(
                id = "1",
                name = "Song One",
                durationMs = 180000
            ),
            Song(
                id = "2",
                name = "Song Two",
                durationMs = 210000
            )
        )

        every {
            getSongsByAlbumUseCase(
                albumId = "album-1",
                offset = 0,
                limit = 10
            )
        } returns flowOf(songs)

        viewModel.loadSongs("album-1")

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(songs, state.songs)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadSongs appends next page to existing songs`() = runTest {
        val firstPage = listOf(
            Song(
                id = "1",
                name = "Song One",
                durationMs = 180000
            ),
            Song(
                id = "2",
                name = "Song Two",
                durationMs = 210000
            )
        )

        val secondPage = listOf(
            Song(
                id = "3",
                name = "Song Three",
                durationMs = 190000
            ),
            Song(
                id = "4",
                name = "Song Four",
                durationMs = 200000
            )
        )

        every {
            getSongsByAlbumUseCase(
                albumId = "album-1",
                offset = 0,
                limit = 10
            )
        } returns flowOf(firstPage)

        every {
            getSongsByAlbumUseCase(
                albumId = "album-1",
                offset = 2,
                limit = 10
            )
        } returns flowOf(secondPage)

        viewModel.loadSongs("album-1")

        advanceUntilIdle()

        viewModel.loadSongs("album-1")

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(
            firstPage + secondPage,
            state.songs
        )

        assertEquals(4, state.songs.size)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadSongs ignores request while loading`() = runTest {
        val songs = listOf(
            Song(
                id = "1",
                name = "Song One",
                durationMs = 180000
            )
        )

        val requestStarted = kotlinx.coroutines.CompletableDeferred<Unit>()
        val continueRequest = kotlinx.coroutines.CompletableDeferred<Unit>()

        every {
            getSongsByAlbumUseCase(
                albumId = "album-1",
                offset = 0,
                limit = 10
            )
        } returns kotlinx.coroutines.flow.flow {
            requestStarted.complete(Unit)
            continueRequest.await()
            emit(songs)
        }

        viewModel.loadSongs("album-1")

        requestStarted.await()

        viewModel.loadSongs("album-1")

        io.mockk.verify(exactly = 1) {
            getSongsByAlbumUseCase(
                albumId = "album-1",
                offset = 0,
                limit = 10
            )
        }

        continueRequest.complete(Unit)

        advanceUntilIdle()

        assertEquals(
            songs,
            viewModel.uiState.value.songs
        )
    }

    @Test
    fun `loadSongs updates error when request fails`() = runTest {
        val exception = RuntimeException("Network error")

        every {
            getSongsByAlbumUseCase(
                albumId = "album-1",
                offset = 0,
                limit = 10
            )
        } returns kotlinx.coroutines.flow.flow {
            throw exception
        }

        viewModel.loadSongs("album-1")

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.songs.isEmpty())
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
    }
}