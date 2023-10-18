package com.example.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.LikedTracksInteractor
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val likeInteractor: LikedTracksInteractor
) : ViewModel() {

    private var timeJob: Job? = null
    private var likeJob: Job? = null
    private var likeIndicator = MutableLiveData<Boolean>()
    private var timer = MutableLiveData("00:00")
    fun createPlayer(url: String, completion: () -> Unit) {
        playerInteractor.createPlayer(url, completion)
    }

    fun play() {
        playerInteractor.play()
        timeJob?.start()!!
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun getTime(): LiveData<String> {
        timeJob = viewModelScope.launch {
            while (true) {
                delay(PLAYER_BUTTON_PRESSING_DELAY)
                playerInteractor.getTime().collect() {
                    timer.postValue(it)
                }
            }
        }
        return timer
    }

    fun putTime(): LiveData<String> {
        getTime()
        return timer
    }

    fun playerStateGetter(): PlayerState {
        return playerInteractor.playerStateGetter()
    }

    fun onLikeClick(track: Track) {
        val id = track.trackId
        if (track.isFavourite) {
            id.let {
                Log.d("Deleting track from favourites", "$track")
                likeInteractor.favouritesDelete(track)
                track.isFavourite = false
            }
        } else id.let {
            Log.d("Pressed like button with", "$track, \"adding to favourites track\"")
            likeInteractor.favouritesAdd(track)
            track.isFavourite = true
        }
    }

    fun onLikedCheck(track: Track): LiveData<Boolean> {
        likeJob = viewModelScope.launch {
            while (true) {
                delay(PLAYER_BUTTON_PRESSING_DELAY)
                track.trackId.let { id ->
                    likeInteractor.favouritesCheck(id)
                        .collect { value ->
                            likeIndicator.postValue(value)
                        }
                }
            }
        }
        return likeIndicator
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}