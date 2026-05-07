package com.example.moodmusic.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.domain.model.MoodEntry
import com.example.moodmusic.ui.theme.AppBackground
import com.example.moodmusic.ui.theme.AppPrimary
import com.example.moodmusic.ui.viewmodel.HistoryViewModel
import com.example.moodmusic.ui.viewmodel.factory.HistoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import com.example.moodmusic.ui.components.BackButton

// Periodos del tab
private enum class HistoryPeriod(val label: String) {
    DAY("Hoy"),
    WEEK("Semana"),
    MONTH("Mes"),
    YEAR("Año")
}

@Composable
fun HistoryScreen(onBackClick: () -> Unit) {
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(AppContainer.moodHistoryRepository)
    )
    val history by viewModel.history.collectAsState()

    var selectedPeriod by remember { mutableStateOf(HistoryPeriod.WEEK) }
    var showChart by remember { mutableStateOf(true) }

    val filtered = remember(history, selectedPeriod) {
        filterByPeriod(history, selectedPeriod)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = onBackClick)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Historial emocional",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        // Tabs de periodo
        val tabs = HistoryPeriod.entries
        TabRow(
            selectedTabIndex = tabs.indexOf(selectedPeriod),
            containerColor = Color.Transparent,
            contentColor = AppPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedPeriod)]),
                    color = AppPrimary
                )
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            tabs.forEach { period ->
                Tab(
                    selected = selectedPeriod == period,
                    onClick = { selectedPeriod = period },
                    text = { Text(period.label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (history.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Todavía no hay emociones guardadas.\nElige una emoción para empezar 🌸",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(32.dp)
                )
            }
            return
        }

        // Toggle gráfica / lista
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { showChart = !showChart }) {
                Text(if (showChart) "📋 Ver lista" else "📊 Ver gráfica")
            }
        }

        if (showChart) {
            // Gráfica de frecuencia de emociones
            EmotionBarChart(
                entries = filtered,
                period = selectedPeriod,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(220.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Lista de entradas
        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Sin emociones en este período",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            // Agrupar por día
            val grouped = remember(filtered) { groupByDay(filtered) }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                grouped.forEach { (dayLabel, entries) ->
                    item {
                        Text(
                            text = dayLabel,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppPrimary,
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                        )
                    }
                    items(entries, key = { it.id }) { entry ->
                        MoodHistoryCard(entry = entry)
                    }
                }
            }
        }
    }
}

// Gráfica de barras

@Composable
private fun EmotionBarChart(
    entries: List<MoodEntry>,
    period: HistoryPeriod,
    modifier: Modifier = Modifier
) {
    // Contar apariciones de cada emoción
    val counts = entries
        .groupBy { it.moodName }
        .mapValues { it.value.size }
        .entries
        .sortedByDescending { it.value }
        .take(8)   // máximo 8 emociones visibles

    if (counts.isEmpty()) return

    val maxCount = counts.maxOf { it.value }.coerceAtLeast(1)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Emociones — ${period.label}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                counts.forEach { (emotion, count) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = "$count",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        val barHeight = ((count.toFloat() / maxCount) * 120).dp
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(barHeight)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(AppPrimary.copy(alpha = 0.75f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Emoji o primeras 3 letras
                        val label = emotion.take(3)
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

// Tarjeta de entrada

    @Composable
    private fun MoodHistoryCard(entry: MoodEntry) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Fondo coloreado para el emoji
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = AppPrimary.copy(alpha = 0.10f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            // El moodName viene como "😄 Alegría" — separamos emoji y texto
                            val parts = entry.moodName.split(" ", limit = 2)
                            Text(
                                text = parts.firstOrNull() ?: "🎵",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    Text(
                        text = entry.moodName.split(" ", limit = 2).getOrElse(1) { entry.moodName },
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Text(
                    text = formatTime(entry.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }

// ── Helpers ────────────────────────────────────────────────────────────────

private fun filterByPeriod(history: List<MoodEntry>, period: HistoryPeriod): List<MoodEntry> {
    val now = System.currentTimeMillis()
    val cutoff = when (period) {
        HistoryPeriod.DAY -> now - 24 * 60 * 60 * 1000L
        HistoryPeriod.WEEK -> now - 7 * 24 * 60 * 60 * 1000L
        HistoryPeriod.MONTH -> now - 30L * 24 * 60 * 60 * 1000L
        HistoryPeriod.YEAR -> now - 365L * 24 * 60 * 60 * 1000L
    }
    return history.filter { it.timestamp >= cutoff }
}

private fun groupByDay(entries: List<MoodEntry>): Map<String, List<MoodEntry>> {
    val dayFmt = SimpleDateFormat("EEEE, d MMMM", Locale("es", "ES"))
    val todayStr = dayFmt.format(Date())
    val yesterdayStr = dayFmt.format(Date(System.currentTimeMillis() - 86_400_000L))

    return entries
        .groupBy { dayFmt.format(Date(it.timestamp)) }
        .mapKeys { (day, _) ->
            when (day) {
                todayStr -> "Hoy"
                yesterdayStr -> "Ayer"
                else -> day.replaceFirstChar { it.uppercase() }
            }
        }
}

private fun formatTime(timestamp: Long): String {
    val fmt = SimpleDateFormat("HH:mm", Locale.getDefault())
    return fmt.format(Date(timestamp))
}
