package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.data.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDataBase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}