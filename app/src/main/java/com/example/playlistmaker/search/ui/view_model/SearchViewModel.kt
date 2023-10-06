package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.ui.SearchScreenState
import kotlinx.coroutines.launch

class SearchViewModel(
    private var searchInteractor: SearchInteractor,
    private var searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {
    private var stateLiveData =
        MutableLiveData<SearchScreenState>()

    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }


    private var trackResultList: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()

    fun searchRequesting(searchExpression: String) {
        if (searchExpression.isNotEmpty()) {
            stateLiveData.postValue(SearchScreenState.Loading)
            viewModelScope.launch {
                stateLiveData.postValue(SearchScreenState.Loading)
                try {
                    searchInteractor.search(searchExpression).collect {
                        when (it.message) {
                            "CONNECTION_ERROR" -> stateLiveData.postValue(SearchScreenState.ConnectionError)
                            "SERVER_ERROR" -> stateLiveData.postValue(SearchScreenState.NothingFound)
                            else -> {
                                trackResultList.postValue(it.data)
                                stateLiveData.postValue(
                                    if (it.data.isNullOrEmpty())
                                        SearchScreenState.NothingFound
                                    else SearchScreenState.SearchIsOk(it.data)
                                )
                            }
                        }
                    }
                } catch (error: Error) {
                    stateLiveData.postValue(SearchScreenState.ConnectionError)
                }
            }
        }
    }

    private var trackHistoryList: MutableLiveData<List<Track>> =
        MutableLiveData<List<Track>>().apply {
            value = emptyList()
        }

    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
    }

    fun provideHistory(): LiveData<List<Track>> {
        val history = searchHistoryInteractor.provideHistory()
        trackHistoryList.value = history!!
        if (history.isNullOrEmpty()) {
            trackHistoryList.postValue(emptyList())
        }
        return trackHistoryList
    }

    fun clearTrackList() {
        trackResultList.value = emptyList()
        stateLiveData.value =
            trackHistoryList.value?.let { SearchScreenState.SearchWithHistory(it) }
    }
}