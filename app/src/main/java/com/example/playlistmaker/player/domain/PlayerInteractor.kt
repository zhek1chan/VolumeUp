package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun play()
    fun pause()
    fun destroy()
    fun createPlayer(url: String, completion: () -> Unit)
    fun getTime(): Flow<String>
    fun playerStateGetter(): PlayerState
}