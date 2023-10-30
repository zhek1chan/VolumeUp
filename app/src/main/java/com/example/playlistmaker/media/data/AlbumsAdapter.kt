package com.example.playlistmaker.media.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class AlbumsAdapter(private val albums: List<Album>) : RecyclerView.Adapter<AlbumsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return AlbumsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        holder.bind(albums[position])
    }
}