package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBackButton = findViewById<ImageView>(R.id.arrow_back)

        arrowBackButton.setOnClickListener {
            finish()
        }
    }
}