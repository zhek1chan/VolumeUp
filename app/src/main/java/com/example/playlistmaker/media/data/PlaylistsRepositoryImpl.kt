package com.example.playlistmaker.media.data

import android.util.Log
import com.example.playlistmaker.media.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.media.data.converters.TrackInPlaylistConvertor
import com.example.playlistmaker.media.data.db.AppDataBase
import com.example.playlistmaker.media.data.entity.PlaylistEntity
import com.example.playlistmaker.media.data.entity.TrackInsidePlaylistEntity
import com.example.playlistmaker.media.data.entity.TracksInPlaylistEntity
import com.example.playlistmaker.media.domain.db.PlaylistsRepository
import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val db: AppDataBase,
    private val convertor: PlaylistDbConvertor,
    private val convertorForTrack: TrackInPlaylistConvertor
) : PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = db.playlistDao().getPlaylist()
        emit(convertFromPlaylistEntity(playlists.reversed()))
    }

    override fun putPlaylist(playlist: Playlist) {
        var converted: PlaylistEntity = convertor.map(playlist)
        db.playlistDao().insertPlaylist(converted)
    }

    override fun deletePlaylist(playlist: Playlist) {
        convertor.map(playlist).let { db.playlistDao().deletePlaylist(playlist.playlistId) }
    }

    override fun checkPlaylist(id: Long): Flow<Boolean> = flow {
        emit(db.playlistDao().queryPlaylistId(id) != null)
    }

    override fun getTracks(id: Long): Flow<List<Track>> = flow {
        db.playlistDao().getTracksFromPlaylist(id)
    }

    override fun putTrack(tracks: TracksInPlaylistEntity) {
        db.playlistDao().addTrackToPlaylist(tracks)
    }

    override fun insertTrack(track: Track) {
        var converted: TrackInsidePlaylistEntity = convertorForTrack.map(track)
        db.playlistDao().insertTrack(converted)
        Log.d("DB", "Inserted $track to DB_playlist")
    }


    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlists -> convertor.map(playlists) }
    }

}