package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.media.data.Playlist
import com.example.playlistmaker.media.data.PlaylistsAdapter
import com.example.playlistmaker.media.data.PlaylistsState
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel.Companion.PLAYER_BUTTON_PRESSING_DELAY
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlaylistsAdapter
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
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_album_cover_corner_radius)))
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
            if (timer == "00:00") {
                updateButton()
            }
        }
        buttonChangerJob?.start()!!

        val bottomSheetContainer = binding.playlistsBottomSheet

        //  BottomSheetBehavior.from() — вспомогательная функция, позволяющая получить объект BottomSheetBehavior, связанный с контейнером BottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.plusButtonPlayerActivity.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.newPlaylist.setOnClickListener {
            Log.d("NewPlaylist", "tap tap")
            findNavController(R.id.root_navigation_graph).navigate(R.id.createPlaylistFragment)
        }
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.loadPlaylists()
        viewModel.observeState().observe(this) {
            render(it)
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // newState — новое состояние BottomSheet
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // загружаем рекламный баннер
                        recyclerView.visibility = View.VISIBLE
                    }

                    else -> {
                        // Остальные состояния не обрабатываем
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        viewModel.onLikedCheck(track).observe(this) { likeIndicator ->
            if (!likeIndicator) {
                changeLikeButton(track)
            } else {
                track.isFavourite = true
                binding.likeButtonPlayerActivity.visibility = View.GONE
                binding.pressedLikeButtonPlayerActivity.visibility = View.VISIBLE
                binding.pressedLikeButtonPlayerActivity.setOnClickListener {
                    Log.d("Press on dislike", ":)")
                    viewModel.onLikeClick(track)
                    changeLikeButton(track)
                }
            }
        }
    }

    override fun onPause() {
        updateButton()
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
        buttonChangerJob = lifecycleScope.launch {
            delay(PLAYER_BUTTON_PRESSING_DELAY)
            playerButtonChanger()
        }
    }

    private fun changeLikeButton(track: Track) {
        binding.likeButtonPlayerActivity.visibility = View.VISIBLE
        binding.pressedLikeButtonPlayerActivity.visibility = View.GONE
        binding.likeButtonPlayerActivity.setOnClickListener {
            Log.d("Press on like button", ":)")
            viewModel.onLikeClick(track)
            binding.likeButtonPlayerActivity.visibility = View.GONE
            binding.pressedLikeButtonPlayerActivity.visibility = View.VISIBLE
        }
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Playlists -> showContent(state.playlist)
            is PlaylistsState.Empty -> showEmpty()
        }
    }

    private fun showContent(playlist: List<Playlist>) {

        recyclerView.adapter = PlaylistsAdapter(playlist)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.recyclerView.visibility = View.GONE
    }
}

