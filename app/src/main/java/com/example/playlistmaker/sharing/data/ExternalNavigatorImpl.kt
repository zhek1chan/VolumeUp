package com.example.playlistmaker.sharing.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ExternalNavigator


class ExternalNavigatorImpl(private val application: Application) : ExternalNavigator {

    override fun shareLink(shareAppLink: String) {

        val shareIntent = Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, shareAppLink)
            this.type = "text/plain"
        }
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(shareIntent)
    }

    override fun openLink() {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(application.getString(R.string.setting_agreement)))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(browserIntent)
    }

    override fun openEmail() {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(application.getString(R.string.settings_email))
        )
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, application.getString(R.string.settings_title))
        shareIntent.putExtra(Intent.EXTRA_TEXT, application.getString(R.string.settings_message))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(shareIntent)
    }

    override fun getShareLink(): String {
        return application.getString(R.string.settings_url)
    }

}