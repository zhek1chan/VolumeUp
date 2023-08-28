package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.SearchHistory
import com.example.playlistmaker.search.domain.SearchHistoryInteractor

class SearchHistoryInteractorImpl(private val historyRepository: SearchHistory) :
    SearchHistoryInteractor {

    override fun addItem(item: Track) {
        historyRepository.addItem(item)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    override fun provideHistory(): List<Track>? {
        return historyRepository.provideHistory()
    }
}