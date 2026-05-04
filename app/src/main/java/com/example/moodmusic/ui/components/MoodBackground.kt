package com.example.moodmusic.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.layout.fillMaxSize
import com.example.moodmusic.domain.model.MoodType

@Composable
fun MoodBackground(
    mood: MoodType,
    modifier: Modifier = Modifier
) {
    when (mood) {

        // 🌧️ Triste → lluvia
        MoodType.SAD -> {
            RainAnimation(
                modifier = modifier.fillMaxSize()
            )
        }

        // ☀️ Feliz → sol
        MoodType.HAPPY -> {
            SunAnimation(
                modifier = modifier.fillMaxSize()
            )
        }

        // 🌿 Relax → ondas suaves
        MoodType.RELAX -> {
            CalmAnimation(
                modifier = modifier.fillMaxSize()
            )
        }

        // ⚡ Energía → animación rápida
        MoodType.ENERGY -> {
            EnergyAnimation(
                modifier = modifier.fillMaxSize()
            )
        }

        // 😡 Enfadado → vibración / rojo
        MoodType.ANGRY -> {
            AngryAnimation(
                modifier = modifier.fillMaxSize()
            )
        }

        // 🧠 Focus → reutilizamos relax
        MoodType.FOCUS -> {
            CalmAnimation(
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun RainAnimation(modifier: Modifier = Modifier) {

    val drops = remember {
        List(40) {
            Pair(
                kotlin.random.Random.nextFloat(),
                kotlin.random.Random.nextFloat()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        drops.forEach { (x, y) ->
            val startY = (y + offsetY) % 1f

            drawLine(
                color = Color(0xFF90CAF9).copy(alpha = 0.15f),
                start = Offset(x * size.width, startY * size.height),
                end = Offset(x * size.width, startY * size.height + 20f),
                strokeWidth = 3f
            )
        }
    }
}

@Composable
fun SunAnimation(modifier: Modifier = Modifier) {

    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        0.9f,
        1.1f,
        animationSpec = infiniteRepeatable(
            tween(1500),
            RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        drawCircle(
            color = Color.Yellow.copy(alpha = 0.3f),
            radius = size.minDimension / 3 * scale
        )
    }
}

@Composable
fun CalmAnimation(modifier: Modifier = Modifier) {

    val infiniteTransition = rememberInfiniteTransition()
    val radius by infiniteTransition.animateFloat(
        100f,
        300f,
        animationSpec = infiniteRepeatable(
            tween(3000),
            RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        drawCircle(
            color = Color(0xFFA5D6A7).copy(alpha = 0.2f),
            radius = radius
        )
    }
}

@Composable
fun EnergyAnimation(modifier: Modifier = Modifier) {

    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        0.2f,
        0.6f,
        animationSpec = infiniteRepeatable(
            tween(300),
            RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(
            color = Color(0xFFFF9F6E).copy(alpha = alpha)
        )
    }
}

@Composable
fun AngryAnimation(modifier: Modifier = Modifier) {

    val infiniteTransition = rememberInfiniteTransition()
    val shift by infiniteTransition.animateFloat(
        -10f,
        10f,
        animationSpec = infiniteRepeatable(
            tween(100),
            RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(
            color = Color(0xFFFF6B6B).copy(alpha = 0.4f)
        )
    }
}

