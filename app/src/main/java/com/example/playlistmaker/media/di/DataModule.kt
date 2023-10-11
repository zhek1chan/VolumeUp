package com.example.playlistmaker.media.di

import androidx.room.Room.databaseBuilder
import com.example.playlistmaker.media.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}