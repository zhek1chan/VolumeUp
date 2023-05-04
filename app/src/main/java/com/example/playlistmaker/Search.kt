package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout

class Search : AppCompatActivity() {
    companion object {
        const val SEARCH_KEY = "SEARCH_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

//        val mediaButton = findViewById<LinearLayout>(R.id.media_layout) // Домашнее задание на разных этапах спринта сильно отличается, поэтому был разработан footer и соответствующие обработчики
//        val settingsButton = findViewById<LinearLayout>(R.id.settings_layout)
        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)

//        arrowBackButton.setOnClickListener { finish() }

//        mediaButton.setOnClickListener {
//            val mediaIntent = Intent(this, Media::class.java)
//            startActivity(mediaIntent)
//        }
//
//        settingsButton.setOnClickListener {
//            val settingsIntent = Intent(this, Settings::class.java)
//            startActivity(settingsIntent)
//        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        outState.putString(SEARCH_KEY, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(SEARCH_KEY, "")
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}