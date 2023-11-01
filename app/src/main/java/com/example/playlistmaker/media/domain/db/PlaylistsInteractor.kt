package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun playlistAdd(playlist: Playlist)
    fun playlistDelete(playlist: Playlist)
    suspend fun playlistGet(): Flow<List<Playlist>>
    suspend fun playlistCheck(id: Long): Flow<Boolean>
}
