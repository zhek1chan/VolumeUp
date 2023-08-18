package com.example.playlistmaker.creator

import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.PlayerRepository

object Creator {
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }

    fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    /*fun getRepository(): PlayerRepositoryImpl {
        return PlayerRepositoryImpl(NetworkClientImpl())
    }

    fun provideTracksInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getRepository())
    }*/
}