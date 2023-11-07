package com.example.playlistmaker.media.data

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class PlaylistsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val pic: ImageView = itemView.findViewById(R.id.emptyList)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val num: TextView = itemView.findViewById(R.id.num)

    fun bind(albums: Playlist) {
        val cornerPixelSize =
            itemView.resources.getDimensionPixelSize(R.dimen.player_album_cover_corner_radius)
        Glide.with(itemView)
            .load(Uri.parse(albums.artworkUrl100))
            .placeholder(R.drawable.song_cover)
            .transform(CenterCrop(), RoundedCorners(cornerPixelSize))
            .into(pic)
        name.text = albums.name
        num.text = albums.num.toString()
    }
}