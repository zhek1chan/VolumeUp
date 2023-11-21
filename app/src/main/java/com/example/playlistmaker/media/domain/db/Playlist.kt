package com.example.playlistmaker.media.domain.db

import android.os.Parcelable
import com.example.playlistmaker.player.domain.Track
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    var playlistId: Long,
    var name: String,
    var description: String,
    var artworkUrl100: String,
    var trackId: Long,
    var num: Long
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val track = other as Track
        return trackId == track.trackId
    }

    override fun hashCode(): Int {
        return trackId.hashCode()
    }
}