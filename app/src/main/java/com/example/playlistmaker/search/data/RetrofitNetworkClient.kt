package com.example.playlistmaker.search.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class RetrofitNetworkClient(
    private val iTunesService: ITunesApi,
    private val context: Context
) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        return if (dto is TrackSearchRequest) {
            val resp = try {
                iTunesService.findTrack(dto.expression).execute()
            } catch (ex: Exception) {
                null
            }
            val body = resp?.body() ?: Response()
            body.apply {
                if (resp != null) {
                    resultCode = resp.code()
                }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }

    //проверка наличия интернета
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}