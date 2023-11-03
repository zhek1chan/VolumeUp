package com.example.playlistmaker.media.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "TracksInPlaylist",
    primaryKeys = ["playlistId", "trackId"],
    foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = ["id"],
        childColumns = ["playlistId"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = TrackEntity::class,
            parentColumns = ["id"],
            childColumns = ["trackId"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class TracksInPlaylistEntity(
    val playlistId: String,
    val tracksId: Long,
)