package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.ThemeSettings

class SettingsInteractorImpl(private var themeSettings: ThemeSettings) : SettingsInteractor {

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