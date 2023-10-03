package com.example.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    private var timeJob: Job? = null
    private var stateLiveData = MutableLiveData<PlayerState>()
    private var timer = MutableLiveData("00:00")
    fun createPlayer(url: String, completion: () -> Unit) {
        playerInteractor.createPlayer(url, completion)
    }

    fun play() {
        playerInteractor.play()
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
        timer.value?.let { Log.d("Timing in PlayerViewModel", it) }
        return timer
    }

    fun playerStateGetter(): PlayerState {
        return playerInteractor.playerStateGetter()
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}