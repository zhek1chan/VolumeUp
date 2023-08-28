package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.Track

interface SearchHistoryInteractor {
    fun addItem(item: Track)
    fun clearHistory()
    fun provideHistory(): List<Track>?
}