package com.example.playlistmaker.media.data

data class Playlist(
    val playlistId: Long,
    var name: String,
    var description: String,
    var artworkUrl100: String,
    var trackId: Long,
    var num: Long
)