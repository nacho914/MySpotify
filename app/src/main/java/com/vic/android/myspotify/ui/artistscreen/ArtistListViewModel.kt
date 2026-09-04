package com.vic.android.myspotify.ui.artistscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vic.android.myspotify.domain.usecase.GetArtistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@HiltViewModel
class ArtistListViewModel @Inject constructor(
    private val getArtistsUseCase: GetArtistsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ArtistListUiState()
    )

    val uiState: StateFlow<ArtistListUiState> =
        _uiState.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 10
    private var isLoadingPage = false

    fun loadArtists() {
        if (isLoadingPage) return

        isLoadingPage = true

        getArtistsUseCase(
            offset = currentOffset,
            limit = pageSize
        )
            .onStart {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )
            }
            .onEach { artists ->
                _uiState.value = _uiState.value.copy(
                    artists = _uiState.value.artists + artists,
                    isLoading = false,
                    error = null
                )

                currentOffset += artists.size
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