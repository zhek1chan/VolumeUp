package com.example.playlistmaker.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.presentation.ui.DateUtils
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.domain.SearchHistory
import com.example.playlistmaker.presentation.ui.adapters.SearchHistoryAdapter
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.adapters.TrackAdapter
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackAdapter.TrackClickListener {

    private lateinit var clearButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var backButton: ImageView
    private lateinit var rvSearchTrack: RecyclerView
    private lateinit var nothingFoundPlaceholder: LinearLayout
    private lateinit var loadingProblemPlaceholder: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var clearSearchHistoryButton: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var loadingIndicator: ProgressBar

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private val tracks = ArrayList<Track>()

    private val trackAdapter = TrackAdapter(this)

    private val tracksInHistory = ArrayList<Track>()

    private val searchHistoryAdapter = SearchHistoryAdapter(this)

    private val searchRunnable = Runnable {
        if (searchEditText.text.toString() != "") { //условие на запуск процесса поиска, если пользователь набрал символ и затем стёр его
            sendRequestToServer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        clearButton = findViewById(R.id.clear_icon)
        searchEditText = findViewById(R.id.input_edit_text)
        backButton = findViewById(R.id.arrow_back)
        rvSearchTrack = findViewById(R.id.search_results_recycler_view)
        nothingFoundPlaceholder = findViewById(R.id.nothing_found_placeholder)
        loadingProblemPlaceholder = findViewById(R.id.net_problem_placeholder)
        refreshButton = findViewById(R.id.update_button)
        rvSearchHistory = findViewById(R.id.search_history_recycler_view)
        searchHistoryLayout = findViewById(R.id.history_layout)
        clearSearchHistoryButton = findViewById(R.id.clear_button)
        loadingIndicator = findViewById(R.id.loading_indicator)

        sharedPreferences = getSharedPreferences(TRACKS_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        trackAdapter.tracks = tracks
        searchHistoryAdapter.searchHistory = tracksInHistory
        rvSearchTrack.adapter = trackAdapter
        rvSearchHistory.adapter = searchHistoryAdapter
        tracksInHistory.addAll(searchHistory.searchedTrackList)
        searchHistoryAdapter.notifyDataSetChanged()


        if (tracksInHistory.size == 0) {
            searchHistoryLayout.visibility = View.GONE
        }

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == TRACKS_LIST_KEY) {
                val tracks = sharedPreferences?.getString(TRACKS_LIST_KEY, null)
                if (tracks != null) {
                    tracksInHistory.clear()
                    tracksInHistory.addAll(createTrackListFromJson(tracks))
                    searchHistoryAdapter.notifyDataSetChanged()
                }
            }
        }

        clearSearchHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            tracksInHistory.clear()
            searchHistoryLayout.visibility = View.GONE
            searchHistoryAdapter.notifyDataSetChanged()
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            searchHistoryLayout.visibility =
                if (hasFocus && searchEditText.text.isEmpty() && tracksInHistory.isNotEmpty()) View.VISIBLE else View.GONE
        }

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("") //стираем текст по нажатию на clearButton
            closeKeyboard()
            tracks.clear()
            rvSearchTrack.visibility = View.GONE
            if ((nothingFoundPlaceholder.visibility == View.VISIBLE) || (loadingProblemPlaceholder.visibility == View.VISIBLE)) {
                nothingFoundPlaceholder.visibility = View.GONE
                loadingProblemPlaceholder.visibility = View.GONE
                rvSearchHistory.visibility = View.GONE
            }
        }


        // обработчик нажатия для кнопки выпадающей клавиатуры
        // IME_ACTION_SEARCH - кнопка поиск (добавляется на клавиатуру)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                closeKeyboard()
                sendRequestToServer()
                true
            }
            false
        }

        refreshButton.setOnClickListener {
            sendRequestToServer()
        }

        val searchTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchHistoryLayout.visibility =
                    if ((searchEditText.hasFocus()) && (tracksInHistory.size == 0) || (loadingProblemPlaceholder.visibility == View.VISIBLE) || (nothingFoundPlaceholder.visibility == View.VISIBLE)) View.GONE else View.VISIBLE
                loadingProblemPlaceholder.visibility = View.GONE
                nothingFoundPlaceholder.visibility = View.GONE
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        searchEditText.addTextChangedListener(searchTextWatcher)
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(
            clearButton.windowToken,
            0
        )  //убираем клавиатуру
    }

    private fun sendRequestToServer() {
        rvSearchTrack.visibility = View.VISIBLE
        nothingFoundPlaceholder.visibility = View.GONE
        loadingProblemPlaceholder.visibility = View.GONE
        loadingIndicator.visibility =
            View.VISIBLE //Меняем видимость элементов перед выполнением запроса
        if (searchEditText.text.isNotEmpty()) { // проверяем, чтобы edittext не был пустым
            iTunesService.findTrack(searchEditText.text.toString()).enqueue(object :
                Callback<TrackResponse> { // вызываем метод findTrack() у iTunesService, и передаем туда текст из edittext
                override fun onResponse(
                    call: Call<TrackResponse>,//метод retrofit
                    response: Response<TrackResponse>
                ) { // метод onResponse() вызывается, когда сервер дал ответ на запрос
                    loadingIndicator.visibility =
                        View.GONE //прячем индикатор загрузки после успешного выполнения запроса
                    if (response.code() == 200) { // code() вызывает код http-статуса, 200 - успех, запрос корректен, сервер вернул ответ
                        tracks.clear() // clear() отчищвет recyclerview от предъидущего списка, без этой строчки отображение нового списка не происходит
                        if (response.body()?.results?.isNotEmpty() == true) { // если ответ(response)  в виде объекта, который указали в Call (body() возвращает) не пустой
                            tracks.addAll(response.body()?.results!!) // добавляем все найденные треки в спсиок addAll() для отображения их на экране
                            trackAdapter.notifyDataSetChanged() // уведомляем адаптер об изменении набора данных, перерисовавается весь набор, это не оптимально
                        } else {
                            trackAdapter.notifyDataSetChanged()
                            rvSearchTrack.visibility = View.GONE
                            nothingFoundPlaceholder.visibility =
                                View.VISIBLE// показываем placeholder nothing_found
                        }
                    } else {
                        tracks.clear()
                        trackAdapter.notifyDataSetChanged()
                        rvSearchTrack.visibility = View.GONE
                        loadingProblemPlaceholder.visibility =
                            View.VISIBLE// показываем loading_problem
                    }
                }

                override fun onFailure(
                    call: Call<TrackResponse>,
                    t: Throwable
                ) { // метод вызывается если не получилось установить соединение с сервером
                    tracks.clear()
                    trackAdapter.notifyDataSetChanged()
                    rvSearchTrack.visibility = View.GONE
                    loadingProblemPlaceholder.visibility = View.VISIBLE
                }

            })
        }
    }

    // Метод делает видимой / невидимой кнопку clearButton
    // в зависимости от того есть или нет текст в поле editText
    // метод возвращает количество последовательности символов
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    //Debounce method
    //если ввод юзера не будет меняться больше двух секунд, то начнётся отправка запроса на сервер
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    //Разрешает юзеру нажимать на элементы списка не чаще одного раза в секунду
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            searchHistory.addNewTrack(track)
            val displayIntent = Intent(this, PlayerActivity::class.java)
            displayIntent
                .putExtra("album cover", track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .putExtra("name song", track.trackName)
                .putExtra("band", track.artistName)
                .putExtra("duration", DateUtils.formatTime(track.trackTimeMillis))
                .putExtra("album", track.collectionName)
                .putExtra("year", DateUtils.formatDate(track.releaseDate))
                .putExtra("genre", track.primaryGenreName)
                .putExtra("country", track.country)
                .putExtra("url", track.previewUrl)

            startActivity(displayIntent)
        }
    }

    // метод дессириализует массив объектов Fact (в Shared Preference они хранятся в виде json строки)
    private fun createTrackListFromJson(json: String?): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    // метод сохраняет поисковой запрос
    override fun onSaveInstanceState(outState: Bundle) {
        searchQuery = searchEditText.text.toString()
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_USER_INPUT, searchQuery)
    }

    // метод восстанавливает поисковой запрос после пересоздания активити
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_USER_INPUT, "")
    }
    private lateinit var searchQuery: String

    companion object {
        const val SEARCH_USER_INPUT = "SEARCH_USER_INPUT"
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

const val TRACKS_PREFERENCES = "tracks_preferences"
const val TRACKS_LIST_KEY = "key_for_tracks_list"
