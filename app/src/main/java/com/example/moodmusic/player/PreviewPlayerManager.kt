package com.example.moodmusic.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class PreviewPlayerManager(context: Context) {

    private val appContext = context.applicationContext

    val player: ExoPlayer = ExoPlayer.Builder(appContext).build()

    private var currentPreviewUrl: String? = null

    fun playOrPause(previewUrl: String?) {
        if (previewUrl.isNullOrBlank()) return

        if (currentPreviewUrl == previewUrl) {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
            return
        }

        currentPreviewUrl = previewUrl
        player.setMediaItem(MediaItem.fromUri(previewUrl))
        player.prepare()
        player.playWhenReady = true
    }

    fun stop() {
        player.stop()
        currentPreviewUrl = null
    }

    fun isCurrent(previewUrl: String?): Boolean {
        return currentPreviewUrl == previewUrl
    }

    fun isPlaying(previewUrl: String?): Boolean {
        return isCurrent(previewUrl) && player.isPlaying
    }

    fun release() {
        player.release()
    }
}