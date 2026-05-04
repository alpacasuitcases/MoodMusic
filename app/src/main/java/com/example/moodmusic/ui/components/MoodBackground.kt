package com.example.moodmusic.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.moodmusic.domain.model.MoodType
import kotlin.math.*
import kotlin.random.Random

@Composable
fun MoodBackground(
    mood: MoodType,
    modifier: Modifier = Modifier
) {
    when (mood) {
        MoodType.SAD    -> RainAnimation(modifier = modifier.fillMaxSize())
        MoodType.HAPPY  -> SunAnimation(modifier = modifier.fillMaxSize())
        MoodType.RELAX  -> WaveAnimation(modifier = modifier.fillMaxSize())
        MoodType.ENERGY -> SparkAnimation(modifier = modifier.fillMaxSize())
        MoodType.ANGRY  -> FireAnimation(modifier = modifier.fillMaxSize())
        MoodType.FOCUS  -> StarFieldAnimation(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun RainAnimation(modifier: Modifier = Modifier) {
    val drops = remember {
        List(60) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat(),
                Random.nextFloat() * 0.5f + 0.5f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "rain")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)),
        label = "rainY"
    )

    Canvas(modifier = modifier) {
        drops.forEach { (x, y, speed) ->
            val startY = (y + offsetY * speed) % 1f
            drawLine(
                color = Color(0xFF90CAF9).copy(alpha = 0.35f),
                start = Offset(x * size.width, startY * size.height),
                end = Offset(x * size.width - 4f, startY * size.height + 28f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun SunAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "sun")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "sunRot"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "sunPulse"
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width * 0.78f, size.height * 0.14f)
        val r = size.minDimension * 0.13f * pulse

        // Halo exterior
        drawCircle(Color(0xFFFFE082).copy(alpha = 0.18f), r * 2.2f, center)
        drawCircle(Color(0xFFFFD54F).copy(alpha = 0.25f), r * 1.6f, center)

        // Rayos
        rotate(rotation, center) {
            repeat(12) { i ->
                val angle = Math.toRadians((i * 30).toDouble())
                val start = Offset(
                    (center.x + cos(angle) * r * 1.25f).toFloat(),
                    (center.y + sin(angle) * r * 1.25f).toFloat()
                )
                val end = Offset(
                    (center.x + cos(angle) * r * 2.0f).toFloat(),
                    (center.y + sin(angle) * r * 2.0f).toFloat()
                )
                drawLine(Color(0xFFFFCC02).copy(alpha = 0.55f), start, end, strokeWidth = 3.5f, cap = StrokeCap.Round)
            }
        }

        // Núcleo
        drawCircle(Color(0xFFFFE57F), r, center)
        drawCircle(Color(0xFFFFF176).copy(alpha = 0.80f), r * 0.65f, center)
    }
}

@Composable
fun WaveAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "wavePhase"
    )

    Canvas(modifier = modifier) {
        repeat(4) { wave ->
            val yBase = size.height * (0.55f + wave * 0.12f)
            val amplitude = 18f - wave * 3f
            val frequency = 1.5f + wave * 0.3f
            val alpha = 0.18f - wave * 0.03f
            val color = Color(0xFF7BDCB5).copy(alpha = alpha)

            var prevX = 0f
            var prevY = yBase
            val step = 8f
            var x = step
            while (x <= size.width) {
                val y = yBase + sin((x / size.width * frequency * 2 * Math.PI + phase).toDouble()).toFloat() * amplitude
                drawLine(color, Offset(prevX, prevY), Offset(x, y), strokeWidth = 4f, cap = StrokeCap.Round)
                prevX = x
                prevY = y
                x += step
            }
        }
    }
}

@Composable
fun SparkAnimation(modifier: Modifier = Modifier) {
    val sparks = remember {
        List(25) {
            Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "spark")
    val tick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, easing = LinearEasing)),
        label = "sparkTick"
    )

    Canvas(modifier = modifier) {
        sparks.forEach { (x, y, phase) ->
            val animPhase = (tick + phase) % 1f
            val alpha = (sin(animPhase * Math.PI).toFloat()).coerceIn(0f, 1f) * 0.70f
            val r = 4f + animPhase * 8f
            drawCircle(
                color = Color(0xFFFFA726).copy(alpha = alpha),
                radius = r,
                center = Offset(x * size.width, y * size.height)
            )
            // Pequeña estrella
            drawCircle(
                color = Color(0xFFFFE082).copy(alpha = alpha * 0.6f),
                radius = r * 0.5f,
                center = Offset(x * size.width, y * size.height)
            )
        }
    }
}

@Composable
fun FireAnimation(modifier: Modifier = Modifier) {
    val particles = remember {
        List(30) {
            Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "fire")
    val tick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing)),
        label = "fireTick"
    )

    Canvas(modifier = modifier) {
        particles.forEach { (x, _, phase) ->
            val animPhase = (tick + phase) % 1f
            val y = 1f - animPhase             // sube de abajo hacia arriba
            val alpha = (1f - animPhase) * 0.50f
            val radius = (1f - animPhase) * 20f + 5f
            val color = if (animPhase < 0.5f)
                Color(0xFFFF5722).copy(alpha = alpha)
            else
                Color(0xFFFF9800).copy(alpha = alpha)

            drawCircle(
                color = color,
                radius = radius,
                center = Offset(x * size.width, y * size.height)
            )
        }
    }
}

@Composable
fun StarFieldAnimation(modifier: Modifier = Modifier) {
    val stars = remember {
        List(40) {
            Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val tick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "starTick"
    )

    Canvas(modifier = modifier) {
        stars.forEach { (x, y, phase) ->
            val animPhase = (tick + phase) % 1f
            val alpha = (0.5f + 0.5f * sin(animPhase * 2 * Math.PI).toFloat()).coerceIn(0f, 1f) * 0.40f
            val r = 2f + phase * 3f
            drawCircle(
                color = Color(0xFFCE93D8).copy(alpha = alpha),
                radius = r,
                center = Offset(x * size.width, y * size.height)
            )
        }
    }
}