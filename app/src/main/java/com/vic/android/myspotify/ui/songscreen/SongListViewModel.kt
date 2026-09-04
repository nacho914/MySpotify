package com.vic.android.myspotify.ui.songscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vic.android.myspotify.domain.usecase.GetSongsByAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val getSongsByAlbumUseCase: GetSongsByAlbumUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SongListUiState()
    )

    val uiState: StateFlow<SongListUiState> =
        _uiState.asStateFlow()

    private var currentAlbumId: String? = null
    private var currentOffset = 0
    private val pageSize = 10
    private var isLoadingPage = false

    fun loadSongs(albumId: String) {

        if (currentAlbumId != albumId) {
            currentAlbumId = albumId
            currentOffset = 0
            isLoadingPage = false

            _uiState.value = SongListUiState()
        }

        if (isLoadingPage) return

        isLoadingPage = true

        getSongsByAlbumUseCase(
            albumId = albumId,
            offset = currentOffset,
            limit = pageSize
        )
            .onStart {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )
            }
            .onEach { songs ->
                currentOffset += songs.size

                _uiState.value = _uiState.value.copy(
                    songs = _uiState.value.songs + songs,
                    isLoading = false,
                    error = null
                )

                isLoadingPage = false
            }
            .catch { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message
                )

                isLoadingPage = false
            }
            .launchIn(viewModelScope)
    }
}