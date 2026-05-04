package com.example.moodmusic.data.repository

import com.example.moodmusic.data.local.dao.PlaylistDao
import com.example.moodmusic.data.local.entity.PlaylistEntity
import com.example.moodmusic.data.local.entity.PlaylistSongEntity
import com.example.moodmusic.domain.model.Song

class PlaylistRepository(private val dao: PlaylistDao) {

    suspend fun savePlaylist(
        playlist: PlaylistEntity,
        songs: List<Song>
    ) {
        dao.insertPlaylist(playlist)

        val songEntities = songs.map {
            PlaylistSongEntity(
                playlistId = playlist.id,
                songId = it.id,
                title = it.title,
                artist = it.artist,
                imageUrl = it.imageUrl,
                previewUrl = it.previewUrl,
                trackUrl = it.trackUrl
            )
        }

        dao.insertSongs(songEntities)
    }

    suspend fun getSongs(playlistId: String): List<Song> {
        return dao.getSongsForPlaylist(playlistId).map {
            Song(
                id = it.songId,
                title = it.title,
                artist = it.artist,
                album = "",
                imageUrl = it.imageUrl,
                previewUrl = it.previewUrl,
                trackUrl = it.trackUrl,
                moodTag = ""
            )
        }
    }

    fun getPlaylists() = dao.getPlaylists()
    suspend fun getPlaylistById(id: String): PlaylistEntity? {
        return dao.getPlaylistById(id)
    }
}