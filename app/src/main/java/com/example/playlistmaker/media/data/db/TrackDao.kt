package com.example.playlistmaker.media.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: List<TrackEntity>)

    @Query("SELECT * FROM tracks_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM tracks_table")
    suspend fun getTracksId(): List<TrackEntity>

    @Query("DELETE FROM tracks_table WHERE TrackId = trackId")
    suspend fun deleteTrack(trackId: String)
}