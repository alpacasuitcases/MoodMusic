package com.example.moodmusic.ui.screens.songs

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.MoodBackground
import com.example.moodmusic.ui.components.SongItem
import com.example.moodmusic.ui.navigation.Routes
import com.example.moodmusic.ui.theme.colorForMood
import com.example.moodmusic.ui.viewmodel.FavoritesViewModel
import com.example.moodmusic.ui.viewmodel.SongsViewModel
import com.example.moodmusic.ui.viewmodel.factory.FavoritesViewModelFactory
import com.example.moodmusic.ui.viewmodel.factory.SongsViewModelFactory

@Composable
fun SongsScreen(
    query: String,
    emotionName: String,
    emoji: String,
    mood: MoodType,
    onBackClick: () -> Unit,
    navController: NavController
) {

    val context = LocalContext.current

    val songsViewModel: SongsViewModel = viewModel(
        factory = SongsViewModelFactory(
            AppContainer.musicRepository,
            AppContainer.playlistRepository
        )
    )

    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(AppContainer.favoritesRepository)
    )

    val songs by songsViewModel.songs.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()

    val previewManager = remember { PreviewPlayerManager(context) }

    // Cargar canciones
    LaunchedEffect(query) {
        songsViewModel.searchSongs(query, mood, emotionName)
    }

    DisposableEffect(Unit) {
        onDispose { previewManager.release() }
    }

    val mood = when (emotionName) {
        "Alegría", "Inspiración", "Ilusión" -> MoodType.HAPPY
        "Motivación", "Energía" -> MoodType.ENERGY
        "Relajo", "Tranquilidad", "Equilibrio", "Comodidad" -> MoodType.RELAX
        "Soledad", "Tristeza", "Cansancio", "Aburrimiento" -> MoodType.SAD
        "Ansiedad", "Enfado", "Frustración" -> MoodType.ANGRY
        else -> MoodType.RELAX
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorForMood(mood),
                        androidx.compose.ui.graphics.Color.White
                    )
                )
            )
    ) {
        val context = LocalContext.current
        TextButton(onClick = onBackClick) {
            Text("← Volver")
        }

        Text(
            text = "$emoji Canciones para $emotionName",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = {
                songsViewModel.saveCurrentPlaylist(
                    name = "Playlist $emotionName"
                )

                Toast.makeText(
                    context,
                    "Playlist guardada",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text("💾 Guardar playlist")
        }
        LazyColumn {

            itemsIndexed(songs) { index, song ->

                val isFavorite = favoriteIds.contains(song.id)

                SongItem(
                    song = song,
                    isFavorite = isFavorite,
                    isPreviewPlaying = previewManager.isPlaying(song.previewUrl),

                    // 🎧 PLAYER
                    onSongClick = {

                        songsViewModel.setPlaylist(songs)

                        navController.navigate(
                            Routes.Player.createRoute(
                                index,
                                mood.name
                            )
                        )
                    },

                    onToggleFavorite = {
                        favoritesViewModel.toggleFavorite(song, isFavorite)
                    },

                    onPreviewClick = {
                        previewManager.playOrPause(song.previewUrl)
                    },

                    onOpenExternal = {
                        song.trackUrl?.let {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            context.startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}