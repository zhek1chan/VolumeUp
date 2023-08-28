package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.Track

interface SearchInteractor {
    fun search(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(findTracks: List<Track>)
    }
}