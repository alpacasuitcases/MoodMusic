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
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.SongItem
import com.example.moodmusic.ui.navigation.Routes
import com.example.moodmusic.ui.viewmodel.FavoritesViewModel
import com.example.moodmusic.ui.viewmodel.SongsViewModel
import com.example.moodmusic.ui.viewmodel.factory.FavoritesViewModelFactory
import com.example.moodmusic.ui.viewmodel.factory.SongsViewModelFactory

@Composable
fun PlaylistDetailScreen(
    playlistId: String,
    onBackClick: () -> Unit,
    navController: NavController
) {

    val songsViewModel: SongsViewModel = viewModel(
        factory = SongsViewModelFactory(
            AppContainer.musicRepository,
            AppContainer.playlistRepository
        )
    )

    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(AppContainer.favoritesRepository)
    )

    val songs by songsViewModel.currentPlaylist.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()

    val context = LocalContext.current
    val previewManager = remember { PreviewPlayerManager(context) }

    var playlist by remember { mutableStateOf<PlaylistEntity?>(null) }

    // Cargar canciones reales desde DB
    LaunchedEffect(playlistId) {
        playlist = AppContainer.playlistRepository.getPlaylistById(playlistId)

        val songs = AppContainer.playlistRepository.getSongs(playlistId)
        songsViewModel.setPlaylist(songs)
    }

    DisposableEffect(Unit) {
        onDispose { previewManager.release() }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo simple (ya no tienes mood aquí)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.DarkGray, Color.Black)
                    )
                )
        )

        Column {

            TextButton(onClick = onBackClick) {
                Text("← Volver")
            }

            Text(
                text = "Playlist",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn {
                itemsIndexed(songs) { index, song ->

                    val isFavorite = favoriteIds.contains(song.id)

                    SongItem(
                        song = song,
                        isFavorite = isFavorite,
                        isPreviewPlaying = previewManager.isPlaying(song.previewUrl),

                        onSongClick = {
                            navController.navigate(
                                Routes.Player.createRoute(
                                    index,
                                    playlist?.mood ?: "HAPPY"
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
}