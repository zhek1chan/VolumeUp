package com.example.playlistmaker.player.domain

interface PlayerRepository {
    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, completion: () -> Unit)
    fun timeTransfer(): String
    fun playerStateReporter(): PlayerState
}