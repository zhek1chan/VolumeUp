package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.converters.TrackDbConvertor
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.entity.TrackEntity
import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LikedTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : LikedTracksRepository {
    override fun getLikedTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun putLikedTrack() {
        TODO("Not yet implemented")
    }

    override fun deleteTrack() {
        TODO("Not yet implemented")
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}