package com.vic.android.myspotify.ui.albumscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vic.android.myspotify.domain.usecase.GetAlbumsByArtistUseCase
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
class AlbumListViewModel @Inject constructor(
    private val getAlbumsByArtistUseCase: GetAlbumsByArtistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AlbumListUiState()
    )

    val uiState: StateFlow<AlbumListUiState> =
        _uiState.asStateFlow()

    fun loadAlbums(artistId: String) {
        getAlbumsByArtistUseCase(artistId)
            .onStart {
                _uiState.value = AlbumListUiState(
                    isLoading = true
                )
            }
            .onEach { albums ->
                _uiState.value = AlbumListUiState(
                    albums = albums
                )
            }
            .catch { exception ->
                _uiState.value = AlbumListUiState(
                    error = exception.message
                )
            }
            .launchIn(viewModelScope)
    }
}