package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.Track

interface SearchHistory {
    fun addItem(newHistoryTrack: Track)
    fun clearHistory()
    fun provideHistory(): List<Track>
}