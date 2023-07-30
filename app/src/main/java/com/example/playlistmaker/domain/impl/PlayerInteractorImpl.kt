package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.presentation.creators.Creator
import com.example.playlistmaker.domain.PlayerState
import com.example.playlistmaker.domain.api.PlayerInteractor

class PlayerInteractorImpl : PlayerInteractor {
    var repository = Creator.providePlayerRepository()

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