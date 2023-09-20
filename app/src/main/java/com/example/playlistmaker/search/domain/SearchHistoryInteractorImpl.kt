package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.Track

class SearchHistoryInteractorImpl(private val historyRepository: SearchHistory) :
    SearchHistoryInteractor {

    override fun addItem(item: Track) {
        historyRepository.addItem(item)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    override fun provideHistory(): List<Track> {
        return historyRepository.provideHistory()
    }
}