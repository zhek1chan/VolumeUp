package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.TracksRepository
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        try {
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            if (response.resultCode != 200) {
                return emptyList()
            }
            return (response as TrackResponse).results.map {
                Track(
                    it.trackName!!,
                    it.artistName!!,
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    it.artworkUrl100!!,
                    it.trackId!!,
                    it.collectionName!!,
                    it.releaseDate!!,
                    it.primaryGenreName!!,
                    it.country!!,
                    it.previewUrl!!
                )
            }
        } catch (t: Throwable) {
            throw Exception(t)
            // TODO:Тут должна быть обработка ошибки
        }
    }
}