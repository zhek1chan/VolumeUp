package com.example.playlistmaker.presentation.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.PlayerState
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.presentation.creators.Creator

class PlayerActivity : AppCompatActivity() {

    lateinit var playerInteractor: PlayerInteractor
    lateinit var playerState: PlayerState
    private lateinit var albumCover: ImageView
    private lateinit var nameSong: TextView
    private lateinit var bandName: TextView
    private lateinit var duration: TextView
    private lateinit var albumName: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var backButton: ImageView
    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    private lateinit var url: String
    private lateinit var handler: Handler
    private lateinit var trackTimer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        albumCover = findViewById(R.id.albums_cover_player_activity)
        nameSong = findViewById(R.id.song_name_player_activity)
        bandName = findViewById(R.id.band_name_player_activity)
        duration = findViewById(R.id.duration_track_value_player_activity)
        albumName = findViewById(R.id.album_value_player_activity)
        year = findViewById(R.id.year_value_player_activity)
        genre = findViewById(R.id.genre_value_player_activity)
        country = findViewById(R.id.country_value_player_activity)
        backButton = findViewById(R.id.back_button_player_activity)
        playButton = findViewById(R.id.play_button_player_activity)
        pauseButton = findViewById(R.id.pause_button_player_activity)
        trackTimer = findViewById(R.id.track_time_player_activity)
        transferDateFromSearchActivity()
        playerInteractor = Creator.providePlayerInteractor()
        playerState = PlayerState.STATE_PAUSED
        if (!url.isEmpty()) playerInteractor.createPlayer(url) {
        }
        handler = Handler(Looper.getMainLooper())
        preparePlayer()
        backButton.setOnClickListener {
            finish()
        }
        playButton.setOnClickListener {
            playerInteractor.play()
        }
        pauseButton.setOnClickListener {
            playerInteractor.pause()
        }
        handler.post(updateButton())
        handler.post(updateTimer())
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.destroy()
    }

    private fun transferDateFromSearchActivity() {
        val arguments: Bundle? = intent.extras

        Glide.with(applicationContext)
            .load(arguments?.getString("album cover"))
            .placeholder(R.drawable.song_cover)
            .centerCrop()
            .into(albumCover)
        nameSong.text = arguments?.getString("name song")
        bandName.text = arguments?.getString("band")
        duration.text = arguments?.getString("duration")
        albumName.text = arguments?.getString("album")
        year.text = arguments?.getString("year")
        genre.text = arguments?.getString("genre")
        country.text = arguments?.getString("country")
        url = arguments?.getString("url").toString()
    }

    private fun preparePlayer() {
        playButton.isEnabled = true
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }

    private fun updateTimer(): Runnable {
        val updatedTimer = Runnable {
            trackTimer.text = playerInteractor.getTime()
            handler.postDelayed(updateTimer(), DELAY_MILLIS_Activity)
        }
        return updatedTimer
    }

    //логика смены кнопок Pause & Play
    private fun playerButtonChanger() {
        playerState = playerInteractor.playerStateGetter()
        when (playerState) {
            PlayerState.STATE_DEFAULT -> {
                playButton.visibility = View.VISIBLE
                pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PREPARED -> {
                playButton.visibility = View.VISIBLE
                pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PAUSED -> {
                playButton.visibility = View.VISIBLE
                pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PLAYING -> {
                pauseButton.visibility = View.VISIBLE
                playButton.visibility = View.GONE
            }
        }
    }

    private fun updateButton(): Runnable {
        val updatedButton = Runnable {
            playerButtonChanger()
            handler.postDelayed(updateButton(), DELAY_MILLIS_Activity)
        }
        return updatedButton
    }

    companion object {
        const val DELAY_MILLIS_Activity = 100L
    }
}

