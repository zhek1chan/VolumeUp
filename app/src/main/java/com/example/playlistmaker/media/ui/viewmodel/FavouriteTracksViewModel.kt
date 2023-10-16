package com.example.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media.data.FavTracksState
import com.example.playlistmaker.media.domain.db.LikedTracksInteractor
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import kotlinx.coroutines.launch

class FavouriteTracksViewModel(
    private val favInteractor: LikedTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavTracksState>()
    fun observeState(): LiveData<FavTracksState> = stateLiveData
    fun fillData() {
        viewModelScope.launch {
            favInteractor
                .favouritesGet()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }


    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavTracksState.Empty(R.string.nothing_in_favourite))
        } else {
            renderState(FavTracksState.FavTracks(tracks))
        }
    }

    private fun renderState(state: FavTracksState) {
        stateLiveData.postValue(state)
    }

    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
    }
}