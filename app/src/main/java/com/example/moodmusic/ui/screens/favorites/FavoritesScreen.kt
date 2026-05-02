package com.example.moodmusic.ui.screens.favorites

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.SongItem
import com.example.moodmusic.ui.theme.AppBackground
import com.example.moodmusic.ui.viewmodel.FavoritesViewModel
import com.example.moodmusic.ui.viewmodel.factory.FavoritesViewModelFactory

@Composable
fun FavoritesScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val previewManager = remember { PreviewPlayerManager(context) }

    val viewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(AppContainer.favoritesRepository)
    )

    val favorites by viewModel.favorites.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            previewManager.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        TextButton(
            onClick = onBackClick,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        ) {
            Text("← Volver")
        }

        Text(
            text = "Tus canciones favoritas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        if (favorites.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Todavía no tienes canciones favoritas",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            return
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {
            items(favorites, key = { it.id }) { song ->
                SongItem(
                    song = song,
                    isFavorite = favoriteIds.contains(song.id),
                    isPreviewPlaying = previewManager.isPlaying(song.previewUrl),
                    onSongClick = {},
                    onToggleFavorite = {
                        viewModel.removeFavorite(song.id)
                    },
                    onPreviewClick = {
                        previewManager.playOrPause(song.previewUrl)
                    },
                    onOpenExternal = {
                        song.trackUrl?.let { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}