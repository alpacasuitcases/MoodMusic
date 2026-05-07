package com.example.moodmusic.ui.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moodmusic.R
import com.example.moodmusic.ui.theme.AppPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {

    // Fase 1: escala de 0 → 1 con bounce
    val scale = remember { Animatable(0f) }
    // Fase 2: rotación continua suave
    val rotation = remember { Animatable(0f) }
    // Fase 3: pulso de escala
    val pulse = remember { Animatable(1f) }
    // Fade del texto
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Entrada con bounce
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
        // Texto aparece
        textAlpha.animateTo(1f, animationSpec = tween(400))

        // Pulso 2 veces
        repeat(2) {
            pulse.animateTo(1.12f, animationSpec = tween(280, easing = FastOutSlowInEasing))
            pulse.animateTo(1f, animationSpec = tween(280, easing = FastOutSlowInEasing))
        }

        // Rotación suave de 0 a 20 grados y vuelta (efecto "wiggle")
        rotation.animateTo(18f, animationSpec = tween(300, easing = FastOutSlowInEasing))
        rotation.animateTo(-18f, animationSpec = tween(300, easing = FastOutSlowInEasing))
        rotation.animateTo(0f, animationSpec = tween(200, easing = FastOutSlowInEasing))

        delay(300L)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE8EAF6), Color(0xFFF7F7FB))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "MoodMusic",
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale.value * pulse.value)
                    .rotate(rotation.value)
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "MoodMusic",
                style = MaterialTheme.typography.headlineLarge,
                color = AppPrimary.copy(alpha = textAlpha.value),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tu música, según tu emoción",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray.copy(alpha = textAlpha.value * 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}