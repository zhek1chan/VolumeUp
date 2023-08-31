package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var playerInteractor: PlayerInteractor
    private lateinit var playerState: PlayerState
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding
    private var handler = Handler(Looper.getMainLooper())
    private var url: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory()
        )[PlayerViewModel::class.java]
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

