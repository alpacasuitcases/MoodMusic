package com.example.moodmusic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
            .padding(vertical = 6.dp)
            .clickable { onSongClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (song.artworkUrl.isNotBlank()) {
                AsyncImage(
                    model = song.artworkUrl,
                    contentDescription = song.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = song.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = song.artist,
                style = MaterialTheme.typography.bodyMedium
            )

            if (song.album.isNotBlank()) {
                Text(
                    text = song.album,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onPreviewClick) {
                    Text(if (isPreviewPlaying) "Pausar preview" else "Escuchar preview")
                }

                OutlinedButton(onClick = onToggleFavorite) {
                    Text(if (isFavorite) "Quitar favorito" else "Favorito")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = onOpenExternal) {
                Text("Abrir canción completa")
            }
        }
    }
}