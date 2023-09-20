package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.ui.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private val favouritesViewModel by viewModel<PlaylistsViewModel>()
    private lateinit var nullablePlaylistsBinding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullablePlaylistsBinding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return nullablePlaylistsBinding.root
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}