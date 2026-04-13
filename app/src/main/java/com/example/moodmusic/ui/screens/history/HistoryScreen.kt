package com.example.moodmusic.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.ui.viewmodel.HistoryViewModel
import com.example.moodmusic.ui.viewmodel.factory.HistoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit
) {
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(AppContainer.moodHistoryRepository)
    )

    val history by viewModel.history.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Volver")
        }

        if (history.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Todavía no hay historial de moods",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            return
        }

        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(history, key = { it.id }) { entry ->
                val formattedDate = rememberFormattedDate(entry.timestamp)

                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = entry.moodName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberFormattedDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}