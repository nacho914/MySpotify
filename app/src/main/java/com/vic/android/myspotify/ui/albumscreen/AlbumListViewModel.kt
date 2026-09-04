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

    private var currentArtistId: String? = null
    private var currentOffset = 0
    private val pageSize = 10
    private var isLoadingPage = false

    fun loadAlbums(artistId: String) {

        if (currentArtistId != artistId) {
            currentArtistId = artistId
            currentOffset = 0
            isLoadingPage = false

            _uiState.value = AlbumListUiState()
        }

        if (isLoadingPage) return

        isLoadingPage = true

        getAlbumsByArtistUseCase(
            artistId = artistId,
            offset = currentOffset,
            limit = pageSize
        )
            .onStart {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )
            }
            .onEach { albums ->
                currentOffset += albums.size

                _uiState.value = _uiState.value.copy(
                    albums = _uiState.value.albums + albums,
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