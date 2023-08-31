package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

lateinit var instance: App
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        Creator.init(this)
        val settingsInteractor = Creator.provideSettingsIneractor()
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

