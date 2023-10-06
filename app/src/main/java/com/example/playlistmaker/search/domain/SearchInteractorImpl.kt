package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.data.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    override fun search(expression: String): Flow<Resource<List<Track>>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success<*> -> {
                    (Resource.Success(result.data))
                }

                is Resource.Error<*> -> {
                    Resource.Error(null, result.message)
                }

                else -> {}
            } as Resource<List<Track>>
        }
    }
}