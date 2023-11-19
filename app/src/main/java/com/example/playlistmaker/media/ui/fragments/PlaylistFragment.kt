package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.data.TracksState
import com.example.playlistmaker.media.domain.db.Playlist
import com.example.playlistmaker.media.ui.viewmodel.PlaylistViewModel
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var pl: Playlist
    private lateinit var trackAdapter: TrackAdapter

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
        binding.time.text = "${collectTimings(pl.playlistId)}  ·  ${pl.num}"
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

                .into(binding.playlistCover)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getTracks(pl.playlistId)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        findNavController().navigate(R.id.mediaFragment)
        super.onDestroyView()
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Tracks -> showContent(state.tracks)
            is TracksState.Empty -> showEmpty()
        }
    }

    private fun showContent(tracks: List<Track>) {
        binding.recyclerView.visibility = View.VISIBLE
        trackAdapter = TrackAdapter {
            clickAdapting(it)
        }
        trackAdapter.setItems(tracks)
        recyclerView.adapter = trackAdapter
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun clickAdapting(item: Track) {
        Log.d("PlaylistFragment", "Click on the track")
        val bundle = Bundle()
        bundle.putParcelable("playlist", item)
        val navController = findNavController()
        navController.navigate(R.id.playerActivity, bundle)
    }

    private fun showEmpty() {
        binding.recyclerView.visibility = View.GONE
    }

    private fun collectTimings(id: Long): String {
        val timeSum = viewModel.getTimeSum(id)
        return "$timeSum минут"
    }

}