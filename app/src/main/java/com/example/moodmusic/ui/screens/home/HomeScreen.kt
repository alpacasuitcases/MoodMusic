package com.example.moodmusic.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onStartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MoodMusic",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Descubre canciones según tu estado de ánimo",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
        )

        Button(onClick = onStartClick) {
            Text("Empezar")
        }
    }
}