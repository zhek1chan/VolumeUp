package com.example.playlistmaker.presentation.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.viewHolders.TrackViewHolder

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