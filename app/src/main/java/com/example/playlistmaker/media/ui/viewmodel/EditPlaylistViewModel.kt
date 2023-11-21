package com.example.playlistmaker.media.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.domain.db.Playlist
import com.example.playlistmaker.media.domain.db.PlaylistsInteractor

class EditPlaylistViewModel(
    private val interactor: PlaylistsInteractor
) : ViewModel() {

    fun savePlayList(pl: Playlist) {
        interactor.playlistAdd(pl)
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        interactor.savePic(uri)
    }
}