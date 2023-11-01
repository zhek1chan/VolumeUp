package com.example.playlistmaker.media.data

sealed interface PlaylistsState {
    data class Playlists(val playlist: List<Playlist>) : PlaylistsState
    data class Empty(val message: Int) : PlaylistsState
}