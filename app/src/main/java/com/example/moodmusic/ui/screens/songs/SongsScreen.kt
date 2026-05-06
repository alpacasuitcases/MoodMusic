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
import com.example.moodmusic.ui.viewmodel.HistoryViewModel
import com.example.moodmusic.ui.viewmodel.SongsViewModel
import com.example.moodmusic.ui.viewmodel.factory.FavoritesViewModelFactory
import com.example.moodmusic.ui.viewmodel.factory.HistoryViewModelFactory
import com.example.moodmusic.ui.viewmodel.factory.SongsViewModelFactory
import com.example.moodmusic.util.NotificationHelper
import com.example.moodmusic.ui.components.BackButton

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

    val historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(AppContainer.moodHistoryRepository)
    )

    val songs by songsViewModel.songs.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()

    val previewManager = remember { PreviewPlayerManager(context) }
    val playingUrl by previewManager.playingUrl.collectAsState()

    // Cargar canciones
    LaunchedEffect(query) {
        songsViewModel.searchSongs(query, mood, emotionName)
    }

    // Guardar en historial
    LaunchedEffect(emotionName) {
        historyViewModel.addMood("$emoji $emotionName")
    }
    DisposableEffect(Unit) {
        onDispose { previewManager.release() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo con gradiente + animación
        Box(
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
        )
        MoodBackground(mood = mood, modifier = Modifier.fillMaxSize())

        Column(modifier = Modifier.fillMaxSize()) {

            BackButton(onClick = onBackClick)

            Text(
                text = "$emoji Canciones para $emotionName",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Button(
                onClick = {
                    songsViewModel.saveCurrentPlaylist(name = "Playlist $emotionName")
                    Toast.makeText(context, "Playlist guardada ✔", Toast.LENGTH_SHORT).show()
                    navController.navigate(
                        Routes.Dashboard.createRoute(
                            mood = mood.name,
                            emotion = Uri.encode("$emoji $emotionName")
                        )
                    ) {
                        // Mantener SongsScreen en la pila para poder volver
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("💾 Guardar playlist y ver opciones")
            }

            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                itemsIndexed(songs) { index, song ->
                    val isFavorite = favoriteIds.contains(song.id)
                    val isPreviewPlaying = playingUrl == song.previewUrl

                    SongItem(
                        song = song,
                        isFavorite = isFavorite,
                        isPreviewPlaying = isPreviewPlaying,

                        onSongClick = {
                            previewManager.stop()
                            AppContainer.playerQueue = songs
                            navController.navigate(Routes.Player.createRoute(index, mood.name))
                        },

                        onToggleFavorite = {
                            favoritesViewModel.toggleFavorite(song, isFavorite)
                            if (!isFavorite) {
                                NotificationHelper.showFavoriteSaved(context, song.title, song.artist)
                            }
                        },

                        onPreviewClick = {
                            previewManager.playOrPause(song.previewUrl)
                        },
                    )
                }
            }
        }
    }
}