package com.example.playlistmaker.media.data

import com.example.playlistmaker.player.domain.Track

sealed interface FavTracksState {
    data class FavTracks(val tracks: List<Track>) : FavTracksState
    data class Empty(val message: Int) : FavTracksState
}
