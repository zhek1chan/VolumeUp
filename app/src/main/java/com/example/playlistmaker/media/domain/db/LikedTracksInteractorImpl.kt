package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

class LikedTracksInteractorImpl(private val repository: LikedTracksRepository) :
    LikedTracksInteractor {
    override fun favouritesAdd(track: Track) {
        repository.putLikedTrack(track)
    }

    override fun favouritesDelete(track: Track) {
        repository.deleteTrack(track)
    }

    override suspend fun favouritesGet(): Flow<List<Track>> {
        return repository.getLikedTracks()
    }

    override suspend fun favouritesCheck(id: Long): Flow<Boolean> {
        return repository.checkOnLike(id)
    }
}