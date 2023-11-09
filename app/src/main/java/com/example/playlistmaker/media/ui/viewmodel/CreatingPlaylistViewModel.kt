package com.example.playlistmaker.media.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.data.Playlist
import com.example.playlistmaker.media.domain.db.PlaylistsInteractor

class CreatingPlaylistViewModel(
    private val interactor: PlaylistsInteractor
) : ViewModel() {
    fun onCreateClick(playlist: Playlist) {
        Log.d("creatingPlaylist", "$playlist, \"adding playlist to DB\"")
        playlist.let { interactor.playlistAdd(it) }
    }
}