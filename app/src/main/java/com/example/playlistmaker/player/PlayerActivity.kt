package com.example.playlistmaker.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

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
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var handler: Handler
    private lateinit var trackTime: TextView
    private var updateTimeRunnable: Runnable = Runnable { }

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
        trackTime = findViewById(R.id.track_time_player_activity)
        transferDateFromSearchActivity()
        handler = Handler(Looper.getMainLooper())
        preparePlayer()
        backButton.setOnClickListener {
            finish()
        }
        playButton.setOnClickListener {
            playbackControl()
        }
        pauseButton.setOnClickListener {
            playbackControl()
        }


    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimeRunnable)
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
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        updateTime()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        pauseButton.visibility = View.GONE
        playButton.visibility = View.VISIBLE
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun updateTime() {
        val text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        trackTime.text = text
        updateTimeRunnable = Runnable { updateTime() }
        handler.postDelayed(updateTimeRunnable, 300)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}