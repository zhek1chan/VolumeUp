package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.data.Resource
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun search(expression: String): Flow<Resource<List<Track>>>

}