package com.example.moodmusic.ui.screens.playlists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.ui.viewmodel.PlaylistsViewModel
import com.example.moodmusic.ui.viewmodel.factory.PlaylistsViewModelFactory
import com.example.moodmusic.data.local.entity.PlaylistEntity
import com.example.moodmusic.ui.components.BackButton

@Composable
fun PlaylistsScreen(
    onBackClick: () -> Unit,
    onPlaylistClick: (PlaylistEntity) -> Unit
) {
    val viewModel: PlaylistsViewModel = viewModel(
        factory = PlaylistsViewModelFactory(AppContainer.playlistRepository)
    )

    val playlists by viewModel.playlists.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        BackButton(onClick = onBackClick)

        Text(
            text = "Tus playlists",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(playlists) { playlist ->
                Card(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    onClick = {
                        onPlaylistClick(playlist)
                    }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = playlist.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text("Mood: ${playlist.mood}")
                    }
                }
            }
        }
    }
}