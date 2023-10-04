package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        try {
            when (response.resultCode) {
                -1 -> {
                    emit(Resource.Error("CONNECTION_ERROR"))
                }

                200 -> {
                    emit(Resource.Success((response as TrackResponse).results.map {
                        Track(
                            it.trackName!!,
                            it.artistName!!,
                            SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(it.trackTimeMillis),
                            it.artworkUrl100!!,
                            it.trackId!!,
                            it.collectionName!!,
                            it.releaseDate!!,
                            it.primaryGenreName!!,
                            it.country!!,
                            it.previewUrl!!
                        )
                    }))
                }

                else -> {
                    emit(Resource.Error("SERVER_ERROR"))
                }
            }
        } catch (error: Error) {
            throw Exception(error)
        }
    }
}