package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.Playlist
import com.example.playlistmaker.media.data.entity.TracksInPlaylistEntity
import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun playlistAdd(playlist: Playlist)
    fun playlistDelete(playlist: Playlist)
    suspend fun playlistGet(): Flow<List<Playlist>>
    suspend fun playlistCheck(id: Long): Flow<Boolean>
    suspend fun getTracks(id: Long): Flow<List<Track>>
    fun putTrack(track: TracksInPlaylistEntity)
    fun insertTrack(track: Track)
}
