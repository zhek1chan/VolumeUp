package com.example.playlistmaker.settings.data

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.ThemeSettings

class SettingsInteractorImpl(private var themeSettings: ThemeSettings) : SettingsInteractor {
    init {
        themeSettings = Creator.provideThemeSettings()
    }

    var isDarkTheme = true

    override fun isAppThemeDark(): Boolean {
        isDarkTheme = themeSettings.lookAtTheme()
        return isDarkTheme
    }

    override fun changeThemeSettings(): Boolean {
        isDarkTheme = themeSettings.appThemeSwitch()
        return isDarkTheme
    }
}