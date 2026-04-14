package com.example.moodmusic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun MoodPicker(onMoodChanged: (Float, Float) -> Unit) {

    var offsetX by remember { mutableStateOf(0.5f) }
    var offsetY by remember { mutableStateOf(0.5f) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val x = change.position.x / size.width
                    val y = change.position.y / size.height

                    offsetX = x.coerceIn(0f, 1f)
                    offsetY = y.coerceIn(0f, 1f)

                    onMoodChanged(offsetX, offsetY)
                }
            }
    ) {

        // 🔥 FONDO EN 4 CUADRANTES
        Row(modifier = Modifier.matchParentSize()) {

            Column(modifier = Modifier.weight(1f)) {
                // arriba izquierda → estrés
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFFF8A80)) // rojo
                )

                // abajo izquierda → triste
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFF90CAF9)) // azul
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                // arriba derecha → feliz
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFFFF176)) // amarillo
                )

                // abajo derecha → relax
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFA5D6A7)) // verde
                )
            }
        }

        // 🔵 PUNTERO
        val dotX = offsetX * constraints.maxWidth
        val dotY = offsetY * constraints.maxHeight

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        dotX.roundToInt() - 20,
                        dotY.roundToInt() - 20
                    )
                }
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black)
        )
    }
}