package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

interface LikedTracksRepository {

    fun getLikedTracks(): Flow<List<Track>>

    fun putLikedTrack(track: Track)

    fun deleteTrack(track: Track)

    fun checkOnLike(id: Long): Flow<Boolean>

}