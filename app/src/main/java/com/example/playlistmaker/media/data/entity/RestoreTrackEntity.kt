package com.example.playlistmaker.media.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "RestoredTracksInPlaylist",
    primaryKeys = ["playlistId", "trackId"],
    foreignKeys = [
        ForeignKey(
            entity = TrackInsidePlaylistEntity::class,
            parentColumns = ["trackId"],
            childColumns = ["trackId"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class RestoredTracksInPlaylistEntity(
    val playlistId: Long,
    val trackId: Long,
)