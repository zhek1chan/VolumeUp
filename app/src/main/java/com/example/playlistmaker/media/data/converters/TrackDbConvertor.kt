package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.entity.TrackEntity
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.data.TrackDto

class TrackDbConvertor {
    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
            track.trackId.toString(),
            track.artworkUrl100.toString(),
            track.trackName.toString(),
            track.artistName.toString(),
            track.collectionName.toString(),
            track.releaseDate.toString(),
            track.primaryGenreName.toString(),
            track.country.toString(),
            track.trackTimeMillis.toString(),
            track.previewUrl.toString()
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId.toLong(),
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}