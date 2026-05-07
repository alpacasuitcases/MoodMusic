package com.example.moodmusic.ui.navigation

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.ui.screens.dashboard.DashboardScreen
import com.example.moodmusic.ui.screens.favorites.FavoritesScreen
import com.example.moodmusic.ui.screens.history.HistoryScreen
import com.example.moodmusic.ui.screens.home.HomeScreen
import com.example.moodmusic.ui.screens.home.SplashScreen
import com.example.moodmusic.ui.screens.mood.MoodSelectionScreen
import com.example.moodmusic.ui.screens.player.PlayerScreen
import com.example.moodmusic.ui.screens.playlists.PlaylistDetailScreen
import com.example.moodmusic.ui.screens.playlists.PlaylistsScreen
import com.example.moodmusic.ui.screens.songs.SongsScreen
import com.example.moodmusic.ui.viewmodel.SongsViewModel
import com.example.moodmusic.ui.viewmodel.factory.SongsViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        // SPLASH
        composable(Routes.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // HOME
        composable(Routes.Home.route) {
            HomeScreen(
                onStartClick = { navController.navigate(Routes.Mood.route) }
            )
        }

        // MOOD SELECTION
        composable(Routes.Mood.route) {
            MoodSelectionScreen(
                onEmotionSelected = { emotion ->
                    navController.navigate(
                        Routes.Songs.createRoute(
                            query = emotion.query,
                            emotionName = emotion.text,
                            emoji = emotion.emoji,
                            mood = emotion.mood.name
                        )
                    )
                },
                onFavorites = { navController.navigate(Routes.Favorites.route) },
                onPlaylists = { navController.navigate(Routes.Playlists.route) },
                onHistory  = { navController.navigate(Routes.History.route) }
            )
        }

        // SONGS
        composable(
            route = Routes.Songs.route,
            arguments = listOf(
                navArgument("query") { type = NavType.StringType },
                navArgument("emotionName") { type = NavType.StringType },
                navArgument("emoji") { type = NavType.StringType },
                navArgument("mood") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val query = Uri.decode(backStackEntry.arguments?.getString("query") ?: "happy music")
            val emotionName = Uri.decode(backStackEntry.arguments?.getString("emotionName") ?: "Alegría")
            val emoji = Uri.decode(backStackEntry.arguments?.getString("emoji") ?: "😄")
            val mood = MoodType.valueOf(backStackEntry.arguments?.getString("mood") ?: "HAPPY")

            SongsScreen(
                query = query,
                emotionName = emotionName,
                emoji = emoji,
                mood = mood,
                onBackClick = { navController.popBackStack() },
                navController = navController
            )
        }

        // DASHBOARD (hub tras guardar playlist)
        composable(
            route = Routes.Dashboard.route,
            arguments = listOf(
                navArgument("mood") { type = NavType.StringType },
                navArgument("emotion") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mood = MoodType.valueOf(backStackEntry.arguments?.getString("mood") ?: "HAPPY")
            val emotion = Uri.decode(backStackEntry.arguments?.getString("emotion") ?: "😄 Alegría")

            DashboardScreen(
                moodLabel = emotion,
                mood = mood,
                onNewMood = {
                    navController.navigate(Routes.Mood.route) {
                        popUpTo(Routes.Home.route)
                    }
                },
                onFavorites = { navController.navigate(Routes.Favorites.route) },
                onHistory = { navController.navigate(Routes.History.route) },
                onPlaylists = { navController.navigate(Routes.Playlists.route) },
                onBackToSongs = { navController.popBackStack() }
            )
        }

        // FAVORITES
        composable(Routes.Favorites.route) {
            FavoritesScreen(onBackClick = { navController.popBackStack() })
        }

        // HISTORY
        composable(Routes.History.route) {
            HistoryScreen(onBackClick = { navController.popBackStack() })
        }

        // PLAYLISTS
        composable(Routes.Playlists.route) {
            PlaylistsScreen(
                onBackClick = { navController.popBackStack() },
                onPlaylistClick = { playlist ->
                    navController.navigate(Routes.PlaylistDetail.createRoute(playlist.id))
                }
            )
        }

        // PLAYLIST DETAIL
        composable(
            route = Routes.PlaylistDetail.route,
            arguments = listOf(
                navArgument("playlistId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getString("playlistId") ?: ""
            PlaylistDetailScreen(
                playlistId = playlistId,
                onBackClick = { navController.popBackStack() },
                navController = navController
            )
        }

        // PLAYER
        composable(
            route = Routes.Player.route,
            arguments = listOf(
                navArgument("index") { type = NavType.IntType },
                navArgument("mood") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val mood = MoodType.valueOf(backStackEntry.arguments?.getString("mood") ?: "HAPPY")

            // Leer la cola guardada por SongsScreen justo antes de navegar
            val songs = AppContainer.playerQueue

            PlayerScreen(
                songs = songs,
                startIndex = index,
                mood = mood,
                navController = navController
            )
        }
    }
}