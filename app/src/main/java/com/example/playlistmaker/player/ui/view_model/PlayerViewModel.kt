package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.PlayerState

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    fun createPlayer(url: String, completion: () -> Unit) {
        playerInteractor.createPlayer(url, completion)
    }

    fun play() {
        playerInteractor.play()
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun getTime(): String {
        return playerInteractor.getTime()
    }

    fun playerStateGetter(): PlayerState {
        return playerInteractor.playerStateGetter()
    }
}