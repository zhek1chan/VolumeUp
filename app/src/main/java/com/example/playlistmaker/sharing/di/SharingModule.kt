package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import org.koin.dsl.module

val sharingModule = module {
    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}