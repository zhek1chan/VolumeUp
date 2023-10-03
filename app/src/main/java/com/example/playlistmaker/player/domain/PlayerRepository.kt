package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, completion: () -> Unit)
    fun timing(): Flow<String>
    fun playerStateReporter(): PlayerState
}