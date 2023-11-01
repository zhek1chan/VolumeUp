package com.example.playlistmaker.media.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(pl: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    fun getPlaylist(): List<PlaylistEntity>

    @Query("SELECT PlaylistId FROM playlists_table")
    fun getPlaylistId(): Flow<List<Integer>>

    @Query("DELETE FROM playlists_table WHERE :playlistsId = PlaylistId")
    fun deletePlaylist(playlistsId: Long): Integer

    @Query("SELECT * FROM playlists_table WHERE :searchId = PlaylistId")
    fun queryPlaylistId(searchId: Long): PlaylistEntity?
}