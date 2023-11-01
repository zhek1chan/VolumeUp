package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylists(): Flow<List<Playlist>>

    fun putPlaylist(playlist: Playlist)

    fun deletePlaylist(playlist: Playlist)

    fun checkPlaylist(id: Long): Flow<Boolean>

}
