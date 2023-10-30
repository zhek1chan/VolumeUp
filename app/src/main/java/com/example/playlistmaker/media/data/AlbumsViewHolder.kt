package com.example.playlistmaker.media.data

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class AlbumsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val pic: ImageView = itemView.findViewById(R.id.emptyList)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val num: TextView = itemView.findViewById(R.id.num)

    fun bind(albums: Album) {
        val cornerPixelSize =
            itemView.resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)
        Glide.with(itemView)
            .load(albums.pic)
            .placeholder(R.drawable.song_cover)
            .centerCrop()
            .transform(RoundedCorners(cornerPixelSize))
            .into(pic)
        name.text = albums.naming
        num.text = albums.num
    }
}