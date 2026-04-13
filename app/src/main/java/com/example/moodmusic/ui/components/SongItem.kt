package com.example.moodmusic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moodmusic.domain.model.Song

@Composable
fun SongItem(
    song: Song,
    isFavorite: Boolean,
    isPreviewPlaying: Boolean,
    onSongClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onPreviewClick: () -> Unit,
    onOpenExternal: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSongClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            if (!song.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = song.imageUrl,
                    contentDescription = song.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = song.title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = song.artist,
                style = MaterialTheme.typography.bodyLarge
            )

            if (song.album.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = song.album,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onPreviewClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isPreviewPlaying) "Pausar preview" else "Escuchar preview")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onToggleFavorite,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isFavorite) "Quitar de favoritos" else "Añadir a favoritos")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onOpenExternal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Abrir canción completa")
            }
        }
    }
}