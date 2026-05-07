package com.example.moodmusic.ui.screens.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moodmusic.data.local.entity.PlaylistEntity
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.domain.model.Song
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.BackButton
import com.example.moodmusic.ui.components.SongItem
import com.example.moodmusic.ui.navigation.Routes
import com.example.moodmusic.ui.viewmodel.FavoritesViewModel
import com.example.moodmusic.ui.viewmodel.factory.FavoritesViewModelFactory
import com.example.moodmusic.util.NotificationHelper
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PlaylistDetailScreen(
    playlistId: String,
    onBackClick: () -> Unit,
    navController: NavController
) {
    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(AppContainer.favoritesRepository)
    )

    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()
    val context = LocalContext.current
    val previewManager = remember { PreviewPlayerManager(context) }
    val playingUrl by previewManager.playingUrl.collectAsState()

    var playlist by remember { mutableStateOf<PlaylistEntity?>(null) }
    var songs    by remember { mutableStateOf<List<Song>>(emptyList()) }

    LaunchedEffect(playlistId) {
        playlist = AppContainer.playlistRepository.getPlaylistById(playlistId)
        songs    = AppContainer.playlistRepository.getSongs(playlistId)
    }

    DisposableEffect(Unit) { onDispose { previewManager.release() } }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo consistente con PlaylistsScreen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460))
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    onClick = onBackClick,
                    tint = Color.White,
                    containerColor = Color.White.copy(alpha = 0.20f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = playlist?.name ?: "Playlist",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    if (!playlist?.mood.isNullOrBlank()) {
                        Text(
                            text = playlist?.mood ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.55f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (songs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                // Contador de canciones
                Text(
                    text = "${songs.size} canciones",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.55f),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    itemsIndexed(songs) { index, song ->
                        val isFavorite       = favoriteIds.contains(song.id)
                        val isPreviewPlaying = playingUrl == song.previewUrl

                        SongItem(
                            song             = song,
                            isFavorite       = isFavorite,
                            isPreviewPlaying = isPreviewPlaying,
                            onSongClick = {
                                previewManager.stop()
                                AppContainer.playerQueue = songs
                                navController.navigate(
                                    Routes.Player.createRoute(index, playlist?.mood ?: "HAPPY")
                                )
                            },
                            onToggleFavorite = {
                                favoritesViewModel.toggleFavorite(song, isFavorite)
                                if (!isFavorite) {
                                    NotificationHelper.showFavoriteSaved(context, song.title, song.artist)
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