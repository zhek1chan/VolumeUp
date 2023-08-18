package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(
    private var externalNavigator: ExternalNavigator,
) : SharingInteractor {
    init {
        externalNavigator = Creator.provideExternalNavigator()
    }

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

    override fun openSupport() {
        externalNavigator.openEmail()
    }

    private fun getShareAppLink(): String {

        return externalNavigator.getShareLink()
    }
}