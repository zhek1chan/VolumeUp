package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.data.entity.PlaylistEntity
import com.example.playlistmaker.media.data.entity.TrackEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDataBase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}