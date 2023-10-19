package com.example.playlistmaker.media.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks_table")
    fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM tracks_table")
    fun getTracksId(): Flow<List<Integer>>

    @Query("DELETE FROM tracks_table WHERE :trackId = TrackId")
    fun deleteTrack(trackId: Long): Integer

    @Query("SELECT * FROM tracks_table WHERE trackId=:searchId")
    fun queryTrackId(searchId: Long): TrackEntity?
}