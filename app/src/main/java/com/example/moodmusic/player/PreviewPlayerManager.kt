package com.example.moodmusic.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewPlayerManager(context: Context) {

    private val appContext = context.applicationContext
    val player: ExoPlayer = ExoPlayer.Builder(appContext).build()

    private val _playingUrl = MutableStateFlow<String?>(null)
    val playingUrl: StateFlow<String?> = _playingUrl

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (!isPlaying && player.playbackState == Player.STATE_ENDED) {
                    _playingUrl.value = null
                }
            }
        })
    }

    fun playOrPause(previewUrl: String?) {
        if (previewUrl.isNullOrBlank()) return

        if (_playingUrl.value == previewUrl) {
            if (player.isPlaying) {
                player.pause()
                _playingUrl.value = null
            } else {
                player.play()
                _playingUrl.value = previewUrl
            }
            return
        }

        _playingUrl.value = previewUrl
        player.stop()
        player.setMediaItem(MediaItem.fromUri(previewUrl))
        player.prepare()
        player.playWhenReady = true
    }
    fun stop() {
        player.stop()
        _playingUrl.value = null
    }

    fun isPlaying(url: String?): Boolean = _playingUrl.value == url && player.isPlaying

    fun release() {
        player.release()
        _playingUrl.value = null
    }
}