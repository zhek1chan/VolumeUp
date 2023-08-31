package com.example.playlistmaker.player.data

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState

class PlayerInteractorImpl : PlayerInteractor {
    private val repository = Creator.providePlayerRepository()

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

    override fun getTime(): String {
        return repository.timeTransfer()
    }

    override fun playerStateGetter(): PlayerState {
        return repository.playerStateReporter()
    }

}