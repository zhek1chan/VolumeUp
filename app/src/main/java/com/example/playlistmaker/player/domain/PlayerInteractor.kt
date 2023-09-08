package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.ui.PlayerState

interface PlayerInteractor {
    fun play()
    fun pause()
    fun destroy()
    fun createPlayer(url: String, completion: () -> Unit)
    fun getTime(): String
    fun playerStateGetter(): PlayerState
}