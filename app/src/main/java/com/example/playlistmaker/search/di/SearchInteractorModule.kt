package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {
    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
}