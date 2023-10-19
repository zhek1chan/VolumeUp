package com.example.playlistmaker.media.di

import androidx.room.Room.databaseBuilder
import com.example.playlistmaker.media.data.converters.TrackDbConvertor
import com.example.playlistmaker.media.data.db.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaDataModule = module {
    single {
        databaseBuilder(androidContext(), AppDataBase::class.java, "database")
            .allowMainThreadQueries()
            .build()
    }
    factory { TrackDbConvertor() }
}