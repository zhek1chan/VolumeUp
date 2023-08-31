package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}