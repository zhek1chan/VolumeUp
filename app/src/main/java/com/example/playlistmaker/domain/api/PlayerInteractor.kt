package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.PlayerState

interface PlayerInteractor {
    fun play()
    fun pause()
    fun destroy()
    fun createPlayer(url: String, completion: () -> Unit)
    fun getTime(): String
    fun playerStateGetter(): PlayerState
}