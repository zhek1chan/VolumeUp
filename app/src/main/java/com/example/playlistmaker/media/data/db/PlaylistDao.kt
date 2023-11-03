package com.example.playlistmaker.media.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.media.data.entity.PlaylistEntity
import com.example.playlistmaker.media.data.entity.TrackEntity
import com.example.playlistmaker.media.data.entity.TracksInPlaylistEntity
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

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addingTrack(tracksInPlaylist: TracksInPlaylistEntity)

    @Query("UPDATE playlists_table SET num = num + 1 WHERE playlistId = :playlistId")
    suspend fun updateQuantity(playlistId: String)

    //отображение всех треков в плейлисте
    @Transaction
    @Query(
        """
        SELECT * FROM playlists_table
        JOIN TracksInPlaylist ON playlists_table.tracksId = TracksInPlaylist.tracksId
        WHERE TracksInPlaylist.playlistId = :playlistId
        """
    )
    fun getTracksFromPlaylist(playlistId: String): Flow<List<TrackEntity>>


    @Query("SELECT EXISTS (SELECT * FROM TracksInPlaylist WHERE playlistId = :playlistId AND tracksId = :trackId)")
    suspend fun checkIfTrackIsInPlaylist(playlistId: String, trackId: Long): Boolean

    @Transaction
    suspend fun addTrackToPlaylist(tInP: TracksInPlaylistEntity): Boolean {
        //проверка на добавление трека до текущего момента
        return if (!checkIfTrackIsInPlaylist(
                tInP.playlistId,
                tInP.tracksId
            )
        ) {
            updateQuantity(tInP.playlistId.toString())
            addingTrack(tInP)
            true
        } else {
            false
        }
    }
}