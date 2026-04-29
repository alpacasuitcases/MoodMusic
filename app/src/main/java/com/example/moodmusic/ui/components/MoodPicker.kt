package com.example.moodmusic.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.moodmusic.domain.model.EmotionLabel
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.domain.model.moodLabels
import kotlin.math.*

@Composable
fun MoodPicker(
    onEmotionChanged: (EmotionLabel) -> Unit
) {
    var offsetX by remember { mutableStateOf(0.5f) }
    var offsetY by remember { mutableStateOf(0.5f) }
    var selectedEmotion by remember { mutableStateOf<EmotionLabel?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val maxRadius = min(size.width, size.height) * 0.39f

                    val raw = change.position
                    val dx = raw.x - center.x
                    val dy = raw.y - center.y
                    val distance = sqrt(dx * dx + dy * dy)

                    val clamped = if (distance > maxRadius) {
                        val scale = maxRadius / distance
                        Offset(center.x + dx * scale, center.y + dy * scale)
                    } else {
                        raw
                    }

                    offsetX = (clamped.x / size.width).coerceIn(0f, 1f)
                    offsetY = (clamped.y / size.height).coerceIn(0f, 1f)

                    selectedEmotion = findClosestEmotion(offsetX, offsetY)
                    selectedEmotion?.let { onEmotionChanged(it) }
                }
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = min(size.width, size.height) * 0.34f
            val innerRadius = baseRadius * 0.18f
            val sideRadius = baseRadius * 0.95f
            val tipRadius = baseRadius * 1.38f
            val sweep = 17f

            moodLabels.forEach { emotion ->
                val selected = emotion == selectedEmotion
                val color = emotionColor(emotion.angle)

                val path = createPetalPath(
                    center = center,
                    innerRadius = innerRadius,
                    sideRadius = sideRadius,
                    tipRadius = tipRadius,
                    angle = emotion.angle,
                    sweep = sweep
                )

                drawPath(
                    path = path,
                    color = color.copy(alpha = if (selected) 0.95f else 0.70f)
                )

                drawPath(
                    path = path,
                    color = Color.White.copy(alpha = 0.88f),
                    style = Stroke(width = if (selected) 5f else 2.4f)
                )
            }

            // Centro de la flor
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFECECF4)
                    ),
                    center = center,
                    radius = baseRadius * 0.24f
                ),
                radius = baseRadius * 0.24f,
                center = center
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.95f),
                radius = baseRadius * 0.24f,
                center = center,
                style = Stroke(width = 3f)
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.45f),
                radius = baseRadius * 0.72f,
                center = center,
                style = Stroke(width = 1.7f)
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.30f),
                radius = baseRadius * 1.08f,
                center = center,
                style = Stroke(width = 1.5f)
            )

            val textPaint = Paint().apply {
                color = android.graphics.Color.rgb(35, 35, 35)
                textSize = 22f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }

            moodLabels.forEach { emotion ->
                val textPosition = polar(
                    center = center,
                    radius = baseRadius * 0.95f,
                    angle = emotion.angle
                )

                drawContext.canvas.nativeCanvas.drawText(
                    emotion.text,
                    textPosition.x,
                    textPosition.y,
                    textPaint
                )
            }
        }

        val dotX = offsetX * constraints.maxWidth
        val dotY = offsetY * constraints.maxHeight

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        dotX.roundToInt() - 13,
                        dotY.roundToInt() - 13
                    )
                }
                .size(26.dp)
                .clip(CircleShape)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(Color.White)
                drawCircle(
                    color = Color(0xFF1F1F1F),
                    radius = size.minDimension * 0.34f
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.85f),
                    radius = size.minDimension / 2f,
                    style = Stroke(width = 2.5f)
                )
            }
        }
    }
}

private fun createPetalPath(
    center: Offset,
    innerRadius: Float,
    sideRadius: Float,
    tipRadius: Float,
    angle: Float,
    sweep: Float
): Path {
    val start = polar(center, innerRadius, angle - sweep * 0.45f)
    val end = polar(center, innerRadius, angle + sweep * 0.45f)
    val tip = polar(center, tipRadius, angle)

    val leftControl = polar(center, sideRadius, angle - sweep)
    val rightControl = polar(center, sideRadius, angle + sweep)

    return Path().apply {
        moveTo(start.x, start.y)
        quadraticTo(leftControl.x, leftControl.y, tip.x, tip.y)
        quadraticTo(rightControl.x, rightControl.y, end.x, end.y)
        close()
    }
}

private fun polar(
    center: Offset,
    radius: Float,
    angle: Float
): Offset {
    val radians = Math.toRadians(angle.toDouble())
    return Offset(
        x = center.x + cos(radians).toFloat() * radius,
        y = center.y + sin(radians).toFloat() * radius
    )
}

private fun emotionColor(angle: Float): Color {
    return when {
        angle < 60f -> Color(0xFFFF6B6B)      // rojo
        angle < 120f -> Color(0xFFFFA94D)     // naranja
        angle < 180f -> Color(0xFFFFE066)     // amarillo
        angle < 240f -> Color(0xFF69DB7C)     // verde
        angle < 300f -> Color(0xFF74C0FC)     // azul
        else -> Color(0xFFB197FC)             // morado
    }
}

private fun findClosestEmotion(x: Float, y: Float): EmotionLabel {
    val dx = x - 0.5f
    val dy = y - 0.5f

    var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
    if (angle < 0f) angle += 360f

    val distance = sqrt(dx * dx + dy * dy) / 0.5f

    return moodLabels.minBy { emotion ->
        val angleDiff = min(
            abs(angle - emotion.angle),
            360f - abs(angle - emotion.angle)
        )

        val distanceDiff = abs(distance - emotion.distance)

        angleDiff + distanceDiff * 60f
    }
}