package com.example.playlistmaker.sharing.domain

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)

    fun openLink()

    fun openEmail()

    fun getShareLink(): String

}