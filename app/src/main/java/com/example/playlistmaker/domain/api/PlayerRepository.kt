package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.PlayerState

interface PlayerRepository {
    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, completion: () -> Unit)
    fun timeTransfer(): String
    fun playerStateReporter(): PlayerState
}