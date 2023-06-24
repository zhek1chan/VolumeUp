package com.example.playlistmaker.history

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Track
import com.example.playlistmaker.TrackViewHolder

class SearchHistotyAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var searchHistory = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(searchHistory[position])
    }

    override fun getItemCount(): Int = searchHistory.size
}