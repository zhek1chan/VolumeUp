package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.ThemeSettings

const val THEME_KEY = "theme"

class ThemeSettingsImpl(private val application: App) : ThemeSettings {
    private var appTheme: Boolean = false
    private lateinit var themeSharedPrefs: SharedPreferences
    override fun lookAtTheme(): Boolean {
        themeSharedPrefs = application.getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE)
        appTheme = themeSharedPrefs.getBoolean(THEME_KEY, !isDarkThemeEnabled())
        return appTheme
    }

    private fun isDarkThemeEnabled(): Boolean {
        val applicationContext = App.instance.applicationContext
        val currentMode =
            applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun appThemeSwitch(): Boolean {
        appTheme = !appTheme
        val editor = themeSharedPrefs.edit()
        editor.putBoolean(THEME_KEY, appTheme)
        editor.apply()
        return appTheme
    }
}