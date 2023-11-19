package com.example.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media.data.TracksState
import com.example.playlistmaker.media.domain.db.PlaylistsInteractor
import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    fun getTracks(id: Long) {
        viewModelScope.launch {
            interactor
                .getTracks(id)
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            stateLiveData.postValue(TracksState.Empty(R.string.nothing_in_favourite))
        } else {
            stateLiveData.postValue(TracksState.Tracks(tracks))
        }
    }

    fun getTimeSum(id: Long): Int {
        var sum = 0
        return sum
    }
}