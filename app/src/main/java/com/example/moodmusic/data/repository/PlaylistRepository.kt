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

        val songEntities = songs.mapIndexed { index, song ->
            PlaylistSongEntity(
                playlistId = playlist.id,
                songId = song.id,
                position = index,
                title = song.title,
                artist = song.artist,
                imageUrl = song.imageUrl,
                previewUrl = song.previewUrl,
                trackUrl = song.trackUrl
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

    suspend fun deletePlaylist(playlist: PlaylistEntity) {
        dao.deleteSongsForPlaylist(playlist.id)
        dao.deletePlaylist(playlist)
    }
}