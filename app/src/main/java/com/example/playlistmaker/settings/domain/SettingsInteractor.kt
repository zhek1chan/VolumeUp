package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun isAppThemeDark(): Boolean
    fun changeThemeSettings(): Boolean
}