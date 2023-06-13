package com.example.playlistmaker.network

import com.example.playlistmaker.network.client.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("search?entity=song")
    fun search(@Query("term") text: String): Call<SearchResponse>
}