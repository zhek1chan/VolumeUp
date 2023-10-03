package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun destroy() {
        repository.destroy()
    }

    override fun createPlayer(url: String, completion: () -> Unit) {
        repository.preparePlayer(url, completion)
    }

    override fun getTime(): Flow<String> {
        return repository.timing()
    }

    override fun playerStateGetter(): PlayerState {
        return repository.playerStateReporter()
    }

}