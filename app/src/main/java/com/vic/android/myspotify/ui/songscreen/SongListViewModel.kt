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

    fun loadSongs(albumId: String) {
        getSongsByAlbumUseCase(albumId)
            .onStart {
                _uiState.value = SongListUiState(
                    isLoading = true
                )
            }
            .onEach { songs ->
                _uiState.value = SongListUiState(
                    songs = songs
                )
            }
            .catch { exception ->
                _uiState.value = SongListUiState(
                    error = exception.message
                )
            }
            .launchIn(viewModelScope)
    }
}