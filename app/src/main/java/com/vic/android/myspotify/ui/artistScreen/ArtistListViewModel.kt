package com.vic.android.myspotify.ui.artistScreen

import androidx.lifecycle.ViewModel
import com.vic.android.myspotify.domain.model.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ArtistListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        ArtistListUiState(
            artists = listOf(
                Artist(
                    id = "1",
                    name = "Coldplay",
                    imageUrl = ""
                ),
                Artist(
                    id = "2",
                    name = "Arctic Monkeys",
                    imageUrl = ""
                ),
                Artist(
                    id = "3",
                    name = "The Weeknd",
                    imageUrl = ""
                )
            )
        )
    )

    val uiState: StateFlow<ArtistListUiState> = _uiState.asStateFlow()
}