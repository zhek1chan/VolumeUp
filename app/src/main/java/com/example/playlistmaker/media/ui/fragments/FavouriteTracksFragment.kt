package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding

class FavouriteTracksFragment : Fragment() {
    //private val favouritesViewModel by viewModel<FavouriteTracksViewModel>()
    private lateinit var nullableFavouriteTracksBinding: FragmentFavouriteTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullableFavouriteTracksBinding =
            FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return nullableFavouriteTracksBinding.root
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }
}