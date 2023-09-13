package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.SearchHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_SHARED_PREFS_KEY = "SEARCH_SHARED_PREFS_KEY"

class SearchHistoryImpl(
    private val savedHistory: SharedPreferences,
    private val gson: Gson
) : SearchHistory {
    private var counter = 0
    private var trackHistoryList = ArrayList<Track>()

    override fun addItem(newHistoryTrack: Track) {
        val json = ""
        if (json.isNotEmpty()) {
            if (trackHistoryList.isEmpty()) {
                if (savedHistory.contains(SEARCH_SHARED_PREFS_KEY)) {
                    val type = object : TypeToken<ArrayList<Track>>() {}.type
                    trackHistoryList = gson.fromJson(json, type)
                }
            }
        }
        if (trackHistoryList.contains(newHistoryTrack)) {
            trackHistoryList.remove(newHistoryTrack)
            trackHistoryList.add(0, newHistoryTrack)
        } else {
            if (trackHistoryList.size < 10) trackHistoryList.add(0, newHistoryTrack)
            else {
                trackHistoryList.removeAt(9)
                trackHistoryList.add(0, newHistoryTrack)
            }
        }
        saveHistory()
    }

    override fun clearHistory() {
        trackHistoryList.clear()
        saveHistory()
    }

    override fun provideHistory(): List<Track> {
        val json = savedHistory.getString(SEARCH_SHARED_PREFS_KEY, "")
        if (json != null) {
            if (json.isNotEmpty()) {
                if (trackHistoryList.isEmpty()) {
                    if (savedHistory.contains(SEARCH_SHARED_PREFS_KEY)) {
                        val type = object : TypeToken<ArrayList<Track>>() {}.type
                        trackHistoryList = gson.fromJson(json, type)
                    }
                }
                Log.d("historyListdata", trackHistoryList.toString())
            } else {
                trackHistoryList = ArrayList()
                Log.d("historyListdata", "empty")
            }
        }
        return trackHistoryList
    }

    private fun saveHistory() {
        var json = ""
        json = gson.toJson(trackHistoryList)
        savedHistory.edit()
            .clear()
            .putString(SEARCH_SHARED_PREFS_KEY, json)
            .apply()
        counter = trackHistoryList.size
    }
}