package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel.Companion.PLAYER_BUTTON_PRESSING_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {
    private lateinit var playerState: PlayerState
    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding
    private var url: String = ""
    private var buttonChangerJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = intent.getParcelableExtra<Track>("track")
        binding.songNamePlayerActivity.text = track?.trackName ?: "Unknown Track"
        binding.bandNamePlayerActivity.text = track?.artistName ?: "Unknown Artist"
        binding.durationTrackValuePlayerActivity.text = track?.trackTimeMillis ?: "00:00"
        binding.albumValuePlayerActivity.text = track?.collectionName ?: "Unknown Album"
        binding.yearValuePlayerActivity.text = (track?.releaseDate ?: "Year").take(4)
        binding.genreValuePlayerActivity.text = track?.primaryGenreName ?: "Unknown Genre"
        binding.countryValuePlayerActivity.text = track?.country ?: "Unknown Country"
        val getImage = (track?.artworkUrl100 ?: "Unknown Cover").replace(
            "100x100bb.jpg",
            "512x512bb.jpg"
        )
        if (getImage != "Unknown Cover") {
            getImage.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(getImage)
                .placeholder(R.drawable.song_cover)
                .into(binding.albumsCoverPlayerActivity)
        }
        url = track?.previewUrl ?: return
        playerState = PlayerState.STATE_PREPARED
        viewModel.createPlayer(url) {
            preparePlayer()
        }

        binding.backButtonPlayerActivity.setOnClickListener {
            finish()
        }
        updateButton()
        binding.playButtonPlayerActivity.setOnClickListener {
            if (viewModel.playerStateGetter() == PlayerState.STATE_PLAYING) viewModel.pause() else viewModel.play()
            updateButton()
        }
        binding.pauseButtonPlayerActivity.setOnClickListener {
            viewModel.pause()
            updateButton()
        }
        viewModel.putTime().observe(this) { timer ->
            binding.trackTimePlayerActivity.text = timer
            if ((timer != "00:00") and (playerState != PlayerState.STATE_PAUSED)) Log.d(
                "TrackTimer",
                timer
            )
        }
        buttonChangerJob?.start()!!
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun preparePlayer() {
        binding.playButtonPlayerActivity.isEnabled = true
        binding.albumPlayerActivity.visibility = View.VISIBLE
        binding.pauseButtonPlayerActivity.visibility = View.GONE
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

    private fun updateButton() {
        Log.d("Changing player button", "updateButton has started")
        buttonChangerJob = lifecycleScope.launch {
            delay(PLAYER_BUTTON_PRESSING_DELAY)
            playerButtonChanger()
        }
    }
}

