package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun formatTime(trackTime: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    }
}