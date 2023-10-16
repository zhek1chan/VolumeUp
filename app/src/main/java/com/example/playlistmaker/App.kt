package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.media.di.mediaDataModule
import com.example.playlistmaker.media.di.mediaModule
import com.example.playlistmaker.media.di.mediaRepositoryModule
import com.example.playlistmaker.player.di.playerModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchInteractorModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.search.di.trackRepositoryModule
import com.example.playlistmaker.settings.di.settingsModule
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.di.sharingModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                playerModule,
                searchInteractorModule,
                searchViewModelModule,
                trackRepositoryModule,
                searchDataModule,
                settingsModule,
                sharingModule,
                mediaModule,
                mediaDataModule,
                mediaRepositoryModule
            )
        }
        instance = this
        val settingsInteractor = getKoin().get<SettingsInteractor>()
        switchTheme(settingsInteractor.isAppThemeDark())
    }

    private fun switchTheme(darkThemeIsEnabled: Boolean) {
        if (darkThemeIsEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    companion object {
        lateinit var instance: App
    }
}

