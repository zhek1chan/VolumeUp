package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.data.Playlist
import com.example.playlistmaker.media.data.PlaylistsAdapter
import com.example.playlistmaker.media.ui.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private val favouritesViewModel by viewModel<PlaylistsViewModel>()
    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylist.setOnClickListener {
            Log.d("NewPlaylist", "tap tap")
            findNavController().navigate(R.id.createPlaylistFragment)
        }
        val playlists = emptyList<Playlist>()
        if (playlists.isEmpty()) {
            binding.emptyLibrary.visibility = View.VISIBLE
            binding.placeholderMessage.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
        val recyclerView = binding.recyclerView

        recyclerView.layoutManager = GridLayoutManager(
            requireContext(), /*Количество столбцов*/
            2
        ) //ориентация по умолчанию — вертикальная
        recyclerView.adapter = PlaylistsAdapter(playlists + playlists + playlists)
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}