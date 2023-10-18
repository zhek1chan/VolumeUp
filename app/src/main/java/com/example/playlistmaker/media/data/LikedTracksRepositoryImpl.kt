package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.converters.TrackDbConvertor
import com.example.playlistmaker.media.data.db.AppDataBase
import com.example.playlistmaker.media.data.entity.TrackEntity
import com.example.playlistmaker.media.domain.db.LikedTracksRepository
import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LikedTracksRepositoryImpl(
    private val db: AppDataBase,
    private val convertor: TrackDbConvertor,
) : LikedTracksRepository {
    override fun getLikedTracks(): Flow<List<Track>> = flow {
        val tracks = db.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks.reversed()))
    }

    override fun putLikedTrack(track: Track) {
        var convertedTrack: TrackEntity = convertor.map(track)
        track.isFavourite = true
        db.trackDao().insertTrack(convertedTrack)
    }

    override fun deleteTrack(track: Track) {
        track.isFavourite = false
        convertor.map(track).let { db.trackDao().deleteTrack(track.trackId) }
    }

    override fun checkOnLike(id: Long): Flow<Boolean> = flow {
        emit(db.trackDao().queryTrackId(id) != null)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> convertor.map(track) }
    }
}