package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.data.Album
import com.example.playlistmaker.media.data.AlbumsAdapter
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
        TODO("NUZHNO DOBAVIT' OBRABOTKU SPISKA ALBOMOV")
        val albums = listOf(
            Album("В Хомяковилле запущена новая станция метро", "Сегодня ", "4"),
            Album("Хомяки вышли на ежегодный марш в поддержку трудящихся", "Ежегодно", "4"),
            Album("В Хомёбино ночью была драка с участием местных банд", "В ночь ", "4"),
            Album("Учёные лаборатории по изучению людей сделали открытие.", "В мышления людей", "4")
        )
        if (albums.isEmpty()) {
            binding.emptyLibrary.visibility = View.VISIBLE
            binding.placeholderMessage.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
        val recyclerView = binding.recyclerView

        recyclerView.layoutManager = GridLayoutManager(
            requireContext(), /*Количество столбцов*/
            2
        ) //ориентация по умолчанию — вертикальная
        recyclerView.adapter = AlbumsAdapter(albums + albums + albums)
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}