package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SearchResultItemBinding
import com.example.playlistmaker.player.domain.Track

class TrackViewHolder(private val binding: SearchResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Track) {
        binding.trackName.text = item.trackName
        binding.trackArtist.text = item.artistName
        val cornerPixelSize =
            itemView.resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)
        binding.trackTime.text = item.trackTimeMillis
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.song_cover)
            .centerCrop()
            .transform(RoundedCorners(cornerPixelSize))
            .into(binding.trackCover)
    }
}
