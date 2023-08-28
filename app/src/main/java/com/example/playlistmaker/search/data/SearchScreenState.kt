package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.Track

sealed class SearchScreenState {
    object DefaultSearch : SearchScreenState()
    object Loading : SearchScreenState()
    object NothingFound : SearchScreenState()
    object ConnectionError : SearchScreenState()
    data class SearchWithHistory(var historyData: List<Track>) : SearchScreenState()
    data class SearchIsOk(val data: List<Track>) : SearchScreenState()
}
