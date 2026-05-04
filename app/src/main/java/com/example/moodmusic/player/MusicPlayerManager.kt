package com.example.moodmusic.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class MusicPlayerManager(context: Context) {

    private val player = ExoPlayer.Builder(context).build()

    fun play(url: String?) {
        if (url == null) return

        val mediaItem = MediaItem.fromUri(url)

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun resume() {
        player.play()
    }

    fun isPlaying(): Boolean = player.isPlaying

    fun release() {
        player.release()
    }
}