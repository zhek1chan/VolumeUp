package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.data.SearchInteractorImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchInteractor
import org.koin.dsl.module

val searchInteractorModule = module {
    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
}