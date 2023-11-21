package com.example.playlistmaker.media.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.data.TracksState
import com.example.playlistmaker.media.domain.db.Playlist
import com.example.playlistmaker.media.ui.PlaylistsBottomAdapter
import com.example.playlistmaker.media.ui.viewmodel.PlaylistViewModel
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewPlaylist: RecyclerView
    private lateinit var pl: Playlist
    private lateinit var trackAdapter: TrackAdapter
    private var sum: String = "0"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pl = arguments?.getParcelable<Playlist>("playlist")!!
        binding.playlistName.text = pl.name
        if (pl.description.isEmpty()) {
            binding.description.text = "2023"
        } else {
            binding.description.text = pl.description
        }
        if (pl.artworkUrl100.isEmpty()) {
            binding.playlistCover.visibility = View.GONE
            binding.playlistPlaceholder.visibility = View.VISIBLE
            binding.buttonBack.visibility = View.VISIBLE
        } else {


            binding.playlistPlaceholder.visibility = View.GONE
            binding.buttonBack.visibility = View.GONE
            binding.playlistCover.visibility = View.VISIBLE
            val uri = pl.artworkUrl100.toUri()
            Glide.with(requireActivity())
                .load(uri)
                .fitCenter()
                .into(binding.playlistCover)

        }
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
        viewModel.getTracks(pl.playlistId)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
            Log.d("Render func", "I have started")
        }
        val overlay = binding.overlay
        val bottomSheetContainerTracks = binding.playlistSongs
        var bottomSheetBehaviorSettings =
            BottomSheetBehavior.from(bottomSheetContainerTracks).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                overlay.visibility = View.VISIBLE
            }
        binding.buttonEdit.setOnClickListener {
            binding.playlistShare.visibility = View.VISIBLE
            val bottomSheetContainerSettings = binding.playlistShare
            binding.share.setOnClickListener { }
            binding.edit.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("playlist", pl)
                val navController = findNavController()
                navController.navigate(R.id.editPlaylistFragment, bundle)
            }
            binding.delete.setOnClickListener {
                viewModel.deletePlaylist(pl)
                findNavController().navigateUp()
            }
            bottomSheetBehaviorSettings =
                BottomSheetBehavior.from(bottomSheetContainerSettings).apply {
                    state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    overlay.visibility = View.VISIBLE
                }
        }

        recyclerViewPlaylist = binding.rvPlaylist
        val list: List<Playlist> = (listOf(pl))
        recyclerViewPlaylist.adapter = PlaylistsBottomAdapter(list) {
            clickAdapting(Track("", ' '.toString(), "", "", 0, "", "", "", "", "", false))
        }
        recyclerView = binding.recyclerView
        trackAdapter = TrackAdapter({
            clickAdapting(it)
        },
            longClickListener = {
                suggestTrackDeleting(it, pl, 0)
            }
        )
        recyclerView.adapter = trackAdapter

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainerTracks).apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
            overlay.visibility = View.VISIBLE
        }
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        bottomSheetContainerTracks.visibility = View.VISIBLE
                        recyclerView.visibility = View.VISIBLE
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        bottomSheetBehaviorSettings.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                        bottomSheetContainerTracks.visibility = View.VISIBLE
                        binding.playlistSongs.visibility = View.VISIBLE
                    }

                    else -> {
                        recyclerViewPlaylist.visibility = View.VISIBLE
                        overlay.visibility = View.VISIBLE
                        bottomSheetContainerTracks.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Tracks -> showContent(state.tracks, pl)
            is TracksState.Empty -> showEmpty()
        }
    }

    private fun showContent(tracks: List<Track>, playlist: Playlist) {
        Log.d("Tracks", "Are shown")
        binding.recyclerView.visibility = View.VISIBLE
        trackAdapter = TrackAdapter({
            clickAdapting(it)
        },
            longClickListener = {
                suggestTrackDeleting(it, playlist, tracks.indexOf(it))
            }
        )
        trackAdapter.setItems(tracks)
        trackAdapter.notifyDataSetChanged()
        recyclerView.adapter = trackAdapter
        recyclerView.adapter?.notifyDataSetChanged()
        binding.time.text = "${viewModel.getTimeSum(tracks)} минуты  ·  ${pl.num}"
        sum = viewModel.getTimeSum(tracks)
    }

    private fun deleteTrackByClick(item: Track, playlist: Playlist, pos: Int) {
        viewModel.deleteTrack(item, playlist)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
            Log.d("New render func", "I have started")
        }
        binding.time.text = "${sum} минут ·  ${pl.num}"
        trackAdapter.notifyDataSetChanged()
    }

    private fun suggestTrackDeleting(track: Track, playlist: Playlist, pos: Int) {

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.question_to_delete))
            .setNegativeButton(getString(R.string.no)) { _, _ ->
                return@setNegativeButton
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteTrackByClick(track, playlist, pos)
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))

    }

    private fun clickAdapting(item: Track) {
        Log.d("PlaylistFragment", "Click on the track")
        val bundle = Bundle()
        bundle.putParcelable("track", item)
        Log.d("track", "$item")
        val navController = findNavController()
        navController.navigate(R.id.fragment_to_playerFragment, bundle)
    }

    private fun showEmpty() {
        Log.d("Tracks", "ARE NOT shown")
        binding.recyclerView.visibility = View.GONE
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf<String>(Manifest.permission.MANAGE_EXTERNAL_STORAGE),
            255
        )
    }

}