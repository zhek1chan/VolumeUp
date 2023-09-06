package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.ITunesApi
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.RetrofitNetworkClient
import com.example.playlistmaker.search.data.SEARCH_SHARED_PREFS_KEY
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {
    single<ITunesApi> {

        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)

    }

    single {
        androidContext()
            .getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }
}