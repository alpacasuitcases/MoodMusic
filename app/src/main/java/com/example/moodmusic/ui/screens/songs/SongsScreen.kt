package com.example.moodmusic.ui.screens.songs

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.BackButton
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

    LaunchedEffect(query) {
        if (songs.isEmpty()) {
            songsViewModel.searchSongs(query, mood, emotionName)
            historyViewModel.addMood("$emoji $emotionName")
        }
    }

    DisposableEffect(Unit) { onDispose { previewManager.release() } }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.Favorites.route) },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.Playlists.route) },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Playlists") },
                    label = { Text("Playlists") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.History.route) },
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                    label = { Text("Historial") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(listOf(colorForMood(mood), Color.White))
                    )
            )
            MoodBackground(mood = mood, modifier = Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                BackButton(onClick = onBackClick)

                Text(
                    text = "$emoji Canciones para $emotionName",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Text(
                    text = "${songs.size} canciones",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
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
                        )
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
                                    Toast.makeText(
                                        context,
                                        "❤️ ${song.title} añadida a favoritos",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            onPreviewClick = {
                                previewManager.playOrPause(song.previewUrl)
                            }
                        )
                    }
                }
            }
        }
    }
}