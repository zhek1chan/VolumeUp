package com.example.playlistmaker.search.ui

import com.example.playlistmaker.player.domain.Track

sealed class SearchScreenState {
    data object DefaultSearch : SearchScreenState()
    data object Loading : SearchScreenState()
    data object NothingFound : SearchScreenState()
    data object ConnectionError : SearchScreenState()
    data class SearchWithHistory(var historyData: List<Track>) : SearchScreenState()
    data class SearchIsOk(val data: List<Track>) : SearchScreenState()
}
