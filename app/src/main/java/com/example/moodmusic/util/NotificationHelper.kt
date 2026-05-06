package com.example.moodmusic.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.moodmusic.R

object NotificationHelper {

    private const val CHANNEL_ID = "moodmusic_favorites"
    private const val CHANNEL_NAME = "Favoritos"
    private var notifId = 0

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Notificaciones de canciones favoritas" }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun showFavoriteSaved(context: Context, songTitle: String, artist: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("❤️ Añadido a favoritos")
            .setContentText("$songTitle — $artist")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
        try {
            NotificationManagerCompat.from(context).notify(notifId++, builder.build())
        } catch (e: SecurityException) {
            // Permiso no concedido — ignorar silenciosamente
        }
    }
}
