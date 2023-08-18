package com.example.playlistmaker.search.domain

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.player.domain.TrackAdapter
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.TrackViewHolder

class SearchHistoryAdapter(val clickListener: TrackAdapter.TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var searchHistory = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(searchHistory[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(searchHistory[position]) }
    }

    override fun getItemCount(): Int = searchHistory.size
}