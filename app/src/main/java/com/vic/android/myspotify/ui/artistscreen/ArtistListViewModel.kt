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
import kotlinx.coroutines.flow.update

@HiltViewModel
class ArtistListViewModel @Inject constructor(
    private val getArtistsUseCase: GetArtistsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistListUiState())
    val uiState: StateFlow<ArtistListUiState> = _uiState.asStateFlow()

    init {
        getArtists()
    }

    private fun getArtists() {
        getArtistsUseCase()
            .onStart {
                _uiState.update {
                    it.copy(isLoading = true)
                }
            }
            .onEach { artists ->
                _uiState.update {
                    it.copy(
                        artists = artists,
                        isLoading = false
                    )
                }
            }
            .catch { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}