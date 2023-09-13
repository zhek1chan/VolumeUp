package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.ui.PlayerState

interface PlayerRepository {
    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, completion: () -> Unit)
    fun timeTransfer(): String
    fun playerStateReporter(): PlayerState
}