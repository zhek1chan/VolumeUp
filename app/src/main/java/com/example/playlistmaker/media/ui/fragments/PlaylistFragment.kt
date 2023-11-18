package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.domain.db.Playlist
import com.example.playlistmaker.media.ui.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.player.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val viewModel by viewModel<PlaylistsViewModel>()
    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var recyclerView: RecyclerView

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
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.mediaFragment)
        }
        val playlist: Playlist = viewModel.getPlaylist()

        if (playlist.artworkUrl100.isEmpty()) {
            binding.playlistPlaceholder.visibility = View.VISIBLE
            binding.buttonBack.visibility = View.VISIBLE
            binding.playlistCover.visibility = View.GONE
        } else {
            binding.playlistPlaceholder.visibility = View.VISIBLE
            binding.playlistCover.visibility = View.GONE
            binding.buttonBack.visibility = View.GONE
        }
        binding.playlistName.text = playlist.name
        binding.description.text = playlist.description
        binding.time.text = collectTimings() + " · " + playlist.num.toString()

        //получение треков
        val tracks: List<Track> = viewModel.getTracks()
        binding.rvPlaylist.adapter //dodelat
        binding.rvPlaylist.layoutManager // dodelat


        viewModel.fillData()
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(
            requireContext(), /*Количество столбцов*/
            2
        ) //ориентация по умолчанию — вертикальная
        viewModel.observeState().observe(viewLifecycleOwner) {
            //render(it)
        }
    }

    override fun onDestroyView() {
        findNavController().navigate(R.id.mediaFragment)
        super.onDestroyView()
    }

    private fun collectTimings(): String {
        val timeSum = viewModel.getTimeSum()
        return "$timeSum минут"
    }

}