package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val nightmodeButton:Switch = findViewById<Switch>(R.id.switch_nightmode)
        nightmodeButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        /*nightmodeButton.setOnCheckedChangeListener{
            isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }*/
        val arrowBackButton = findViewById<ImageView>(R.id.arrow_back)
        arrowBackButton.setOnClickListener {
            finish()
        }
        val shareButton = findViewById<LinearLayout>(R.id.button_share)
        shareButton.setOnClickListener {
            var shareIntent = Intent().apply{
                this.action=Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT,"https://practicum.yandex.ru/android-developer/")
                this.type="text/plain"
            }
            startActivity(shareIntent)
        }
        val helpButton = findViewById<LinearLayout>(R.id.button_help)
        helpButton.setOnClickListener {
            val message = "Привет, Android разработка — это круто!"
            val title = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("evg.ermolov@ya.ru"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }
        val urlButton = findViewById<LinearLayout>(R.id.button_user_agreements)
        urlButton.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(browserIntent)
        }
    }
}