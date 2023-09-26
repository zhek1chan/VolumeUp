package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.ThemeSettingsImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.ThemeSettings
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    single<ThemeSettings> {
        ThemeSettingsImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    viewModel { SettingsViewModel(get(), get()) }
}