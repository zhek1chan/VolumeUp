package com.example.playlistmaker.player.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.SearchResultItemBinding
import com.example.playlistmaker.player.ui.TrackViewHolder

class TrackAdapter(
    private val clickListener: TrackClick
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var _items: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(SearchResultItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(_items[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(_items[position])
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return _items.size
    }

    fun interface TrackClick {
        fun onClick(track: Track)
    }

    fun setItems(items: List<Track>) {
        _items = items
        notifyDataSetChanged()
    }
}