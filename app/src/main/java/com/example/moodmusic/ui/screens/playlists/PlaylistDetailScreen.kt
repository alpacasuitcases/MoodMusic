package com.example.moodmusic.ui.screens.playlists

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodmusic.data.local.entity.PlaylistEntity
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.domain.model.Song
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.SongItem
import com.example.moodmusic.ui.navigation.Routes
import com.example.moodmusic.ui.viewmodel.FavoritesViewModel
import com.example.moodmusic.ui.viewmodel.factory.FavoritesViewModelFactory

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

    // Cargar canciones reales desde DB
    LaunchedEffect(playlistId) {
        playlist = AppContainer.playlistRepository.getPlaylistById(playlistId)
        songs = AppContainer.playlistRepository.getSongs(playlistId)
    }

    DisposableEffect(Unit) {
        onDispose { previewManager.release() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF2C2C54), Color.Black)))
        )

        Column {
            TextButton(onClick = onBackClick) { Text("← Volver", color = Color.White) }

            Text(
                text = playlist?.name ?: "Playlist",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            if (songs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                LazyColumn {
                    itemsIndexed(songs) { index, song ->
                        val isFavorite      = favoriteIds.contains(song.id)
                        val isPreviewPlaying = playingUrl == song.previewUrl

                        SongItem(
                            song             = song,
                            isFavorite       = isFavorite,
                            isPreviewPlaying = isPreviewPlaying,

                            onSongClick = {
                                // Guardar lista con orden correcto y navegar
                                previewManager.stop()
                                AppContainer.playerQueue = songs
                                navController.navigate(
                                    Routes.Player.createRoute(index, playlist?.mood ?: "HAPPY")
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
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}