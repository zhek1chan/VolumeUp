package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {
    override fun playlistAdd(playlist: Playlist) {
        repository.putPlaylist(playlist)
    }

    override suspend fun playlistGet(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun playlistCheck(id: Long): Flow<Boolean> {
        return repository.checkPlaylist(id)
    }

    override fun playlistDelete(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }
}