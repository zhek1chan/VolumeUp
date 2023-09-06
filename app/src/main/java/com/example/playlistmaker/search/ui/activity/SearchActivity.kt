package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.domain.TrackAdapter
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.data.SearchScreenState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel by viewModel<SearchViewModel>()
    private var isClickAllowed = true
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var historyRecycler: RecyclerView
    lateinit var historyList: List<Track>
    private lateinit var recyclerView: RecyclerView
    private val handler = Handler(Looper.getMainLooper())
    private var isEnterPressed: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        historyRecycler = binding.searchHistoryRecyclerView
        searchViewModel.getStateLiveData().observe(this) { stateLiveData ->
            when (val state = stateLiveData) {
                is SearchScreenState.DefaultSearch -> defaultSearch()
                is SearchScreenState.ConnectionError -> connectionError()
                is SearchScreenState.Loading -> loading()
                is SearchScreenState.NothingFound -> nothingFound()
                is SearchScreenState.SearchIsOk -> searchIsOk(state.data)
                is SearchScreenState.SearchWithHistory -> searchWithHistory(state.historyData)
                else -> {
                    connectionError()
                }
            }
        }

        binding.arrowBack.setOnClickListener {
            finish()
        }

        onEditorFocus()
        onSearchTextChange()
        onClearIconClick()
        clearIconVisibilityChanger()
        startSearchByEnterPress()

        trackAdapter = TrackAdapter() {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }

        recyclerView = binding.searchResultsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        //
        historyAdapter = TrackAdapter() {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
        binding.clearButton.setOnClickListener {
            makeHistoryLLGone()
            searchViewModel.clearHistory()
        }

        historyList = try {
            val historyValue = searchViewModel.provideHistory().value
            historyValue ?: emptyList()
        } catch (e: IOException) {
            emptyList()
        }
        //searchViewModel.provideHistory()
        if (historyList.isNotEmpty()) {
            makeHistoryLLVisible()
        }
    }

    // метод сохраняет поисковой запрос
    override fun onSaveInstanceState(outState: Bundle) {
        searchText = binding.inputEditText.text.toString()
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_USER_INPUT, searchText)
    }

    // метод восстанавливает поисковой запрос после пересоздания активити
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_USER_INPUT, "")
    }

    private lateinit var searchText: String

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun clickAdapting(item: Track) {
        searchViewModel.addItem(item)
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("track", item)
        this.startActivity(intent)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search() {
        searchViewModel.searchRequesting(binding.inputEditText.text.toString())
    }


    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    private val searchRunnable = Runnable {
        search()
    }

    private fun onEditorFocus() {
        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && searchViewModel.provideHistory().value?.isNotEmpty() == true) {
                searchViewModel.clearTrackList()
            } else {
                makeHistoryLLGone()
            }
        }
    }

    private fun onSearchTextChange() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.inputEditText.hasFocus() && p0?.isEmpty() == true && historyList.isNotEmpty()) {
                    searchViewModel.clearTrackList()
                } else {
                    makeHistoryLLGone()
                }
                if (!binding.inputEditText.text.isNullOrEmpty()) {
                    searchText = binding.inputEditText.text.toString()
                    searchDebounce()

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun startSearchByEnterPress() {
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputEditText.text.isNotEmpty()) {
                    searchText = binding.inputEditText.text.toString()
                    search()
                    trackAdapter.notifyDataSetChanged()
                    isEnterPressed = true
                    handler.postDelayed({ isEnterPressed = false }, 3000L)
                }
            }
            false
        }
    }

    private fun onClearIconClick() {
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.inputEditText.windowToken,
                0
            )
            binding.inputEditText.clearFocus()
            searchViewModel.clearTrackList()
        }
    }

    private fun clearIconVisibilityChanger() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun defaultSearch() {
        makeHistoryLLGone()
        recyclerView.visibility = View.VISIBLE
        binding.nothingFoundPlaceholder.visibility = View.GONE
        binding.netProblemPlaceholder.visibility = View.GONE
        Log.d("DefaultSearch", "DefaultSearch was started")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loading() {
        binding.loadingIndicator.visibility = View.VISIBLE
        makeHistoryLLGone()
        recyclerView.visibility = View.GONE
        binding.nothingFoundPlaceholder.visibility = View.GONE
        binding.netProblemPlaceholder.visibility = View.GONE
        trackAdapter.notifyDataSetChanged()
        Log.d("Loading", "Loading was started")
    }

    private fun searchIsOk(data: List<Track>) {
        binding.loadingIndicator.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        binding.nothingFoundPlaceholder.visibility = View.GONE
        binding.netProblemPlaceholder.visibility = View.GONE
        binding.clearButton.visibility - View.GONE
        trackAdapter.setItems(data)
        makeHistoryLLGone()
        Log.d("SearchIsOk", "Loading has been end")
    }

    private fun nothingFound() {
        binding.loadingIndicator.visibility = View.GONE
        binding.historyLayout.visibility = View.GONE
        historyRecycler.visibility = View.GONE
        binding.clearButton.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        binding.nothingFoundPlaceholder.visibility = View.VISIBLE
        binding.netProblemPlaceholder.visibility = View.GONE
        makeHistoryLLGone()
        Log.d("NothingFound", "NothingFound")
    }

    private fun connectionError() {
        binding.loadingIndicator.visibility = View.GONE
        binding.netProblemPlaceholder.visibility = View.VISIBLE
        binding.nothingFoundPlaceholder.visibility = View.GONE
        recyclerView.visibility = View.GONE
        binding.updateButton.setOnClickListener { search() }
        makeHistoryLLGone()
        Log.d("ConnectionError", "Connection Error")
    }

    private fun searchWithHistory(historyData: List<Track>) {
        makeHistoryLLVisible()
        binding.clearButton.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        binding.nothingFoundPlaceholder.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.netProblemPlaceholder.visibility = View.GONE
        binding.loadingIndicator.visibility = View.GONE
        historyAdapter.setItems(historyData)
        historyAdapter.notifyDataSetChanged()
        Log.d("searchWithHistory", "SearchWithHistory was started")
    }

    private fun makeHistoryLLGone() {
        binding.historyLayout.visibility = View.GONE
    }

    private fun makeHistoryLLVisible() {
        binding.historyLayout.visibility = View.VISIBLE
    }


    companion object {
        const val SEARCH_USER_INPUT = "SEARCH_USER_INPUT"
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

