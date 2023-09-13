package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.SearchHistoryImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistory
import com.example.playlistmaker.search.domain.TracksRepository
import org.koin.dsl.module

val trackRepositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }
    single<SearchHistory> {
        SearchHistoryImpl(get(), get())
    }
}