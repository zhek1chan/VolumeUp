package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

interface LikedTracksInteractor {
    suspend fun favouritesAdd(track: Track)
    suspend fun favouritesDelete(track: Track)
    suspend fun favouritesGet(): Flow<List<Track>>
    suspend fun favouritesCheck(id: String): Flow<Boolean>
}
