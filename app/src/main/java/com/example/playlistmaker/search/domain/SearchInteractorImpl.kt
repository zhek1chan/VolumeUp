package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.Track

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    override fun search(
        expression: String,
        consumer: SearchInteractor.TracksConsumer
    ) {
        var tracksData: List<Track>
        val t = Thread {
            tracksData = repository.searchTracks(expression)
            consumer.consume(tracksData)
        }
        t.start()
    }
}