package com.example.playlistmaker.media.ui.viewmodel.playlists

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.TracksState
import com.example.playlistmaker.media.domain.db.Playlist
import com.example.playlistmaker.media.domain.db.PlaylistsInteractor
import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val interactor: PlaylistsInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    fun savePlayList(pl: Playlist, tracks: List<Track>) {
        interactor.playlistAdd(pl, tracks)
    }

    fun getTracks(id: Long) {
        Log.d("getTracksMethod", "has been started")
        viewModelScope.launch {
            interactor
                .getTracks(id)
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        stateLiveData.postValue(TracksState.Tracks(tracks))
        Log.d("Restoring tracks", "$tracks")
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        interactor.savePic(uri)
    }
}