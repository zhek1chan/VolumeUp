package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.data.LikedTracksRepositoryImpl
import com.example.playlistmaker.media.domain.db.LikedTracksInteractor
import com.example.playlistmaker.media.domain.db.LikedTracksInteractorImpl
import com.example.playlistmaker.media.domain.db.LikedTracksRepository
import org.koin.dsl.module

val mediaRepositoryModule = module {
    single<LikedTracksRepository> {
        LikedTracksRepositoryImpl(get(), get())
    }
    single<LikedTracksInteractor> {
        LikedTracksInteractorImpl(get())
    }
}