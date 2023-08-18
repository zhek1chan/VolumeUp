package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var playerInteractor: PlayerInteractor
    private lateinit var playerState: PlayerState
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var handler: Handler
    private lateinit var url: String
    private lateinit var nameSong: TextView
    private lateinit var bandName: TextView
    private lateinit var duration: TextView
    private lateinit var albumName: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory()
        )[PlayerViewModel::class.java]
        nameSong = findViewById(R.id.song_name_player_activity)
        bandName = findViewById(R.id.band_name_player_activity)
        duration = findViewById(R.id.duration_track_value_player_activity)
        albumName = findViewById(R.id.album_value_player_activity)
        year = findViewById(R.id.year_value_player_activity)
        genre = findViewById(R.id.genre_value_player_activity)
        country = findViewById(R.id.country_value_player_activity)

        transferDateFromSearchActivity()
        playerInteractor = Creator.providePlayerInteractor()
        playerState = PlayerState.STATE_PREPARED

        viewModel.createPlayer(url) {
            preparePlayer()
        }
        handler = Handler(Looper.getMainLooper())

        binding.backButtonPlayerActivity.setOnClickListener {
            finish()
        }
        binding.playButtonPlayerActivity.setOnClickListener {
            viewModel.play()
        }
        binding.pauseButtonPlayerActivity.setOnClickListener {
            viewModel.pause()
        }
        handler.post(updateButton())
        handler.post(updateTimer())
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }

    private fun transferDateFromSearchActivity() {
        val arguments: Bundle? = intent.extras

        Glide.with(applicationContext)
            .load(arguments?.getString("album cover"))
            .placeholder(R.drawable.song_cover)
            .centerCrop()
            .into(binding.albumsCoverPlayerActivity)
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
        binding.playButtonPlayerActivity.isEnabled = true
        binding.albumPlayerActivity.visibility = View.VISIBLE
        binding.pauseButtonPlayerActivity.visibility = View.GONE
    }

    private fun updateTimer(): Runnable {
        val updatedTimer = Runnable {
            binding.trackTimePlayerActivity.text = viewModel.getTime()
            handler.postDelayed(updateTimer(), DELAY_MILLIS_Activity)
        }
        return updatedTimer
    }

    //логика смены кнопок Pause & Play
    private fun playerButtonChanger() {
        playerState = viewModel.playerStateGetter()
        when (playerState) {
            PlayerState.STATE_DEFAULT -> {
                binding.playButtonPlayerActivity.visibility = View.VISIBLE
                binding.pauseButtonPlayerActivity.visibility = View.GONE
            }

            PlayerState.STATE_PREPARED -> {
                binding.playButtonPlayerActivity.visibility = View.VISIBLE
                binding.pauseButtonPlayerActivity.visibility = View.GONE
            }

            PlayerState.STATE_PAUSED -> {
                binding.playButtonPlayerActivity.visibility = View.VISIBLE
                binding.pauseButtonPlayerActivity.visibility = View.GONE
            }

            PlayerState.STATE_PLAYING -> {
                binding.pauseButtonPlayerActivity.visibility = View.VISIBLE
                binding.playButtonPlayerActivity.visibility = View.GONE
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

