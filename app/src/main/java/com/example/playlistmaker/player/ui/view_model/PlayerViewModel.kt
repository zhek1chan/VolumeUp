package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState

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

    fun destroy() {
        playerInteractor.destroy()
    }

    fun getTime(): String {
        return playerInteractor.getTime()
    }

    fun playerStateGetter(): PlayerState {
        return playerInteractor.playerStateGetter()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                // 1
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        Creator.providePlayerInteractor()
                    ) as T
                }
            }
    }
}