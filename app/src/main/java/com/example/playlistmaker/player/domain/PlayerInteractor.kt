package com.example.playlistmaker.player.domain

interface PlayerInteractor {
    fun play()
    fun pause()
    fun destroy()
    fun createPlayer(url: String, completion: () -> Unit)
    fun getTime(): String
    fun playerStateGetter(): PlayerState
}