package com.example.moodmusic.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.ui.screens.favorites.FavoritesScreen
import com.example.moodmusic.ui.screens.history.HistoryScreen
import com.example.moodmusic.ui.screens.playlists.PlaylistsScreen
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.ui.screens.playlists.PlaylistDetailScreen
import com.example.moodmusic.ui.screens.home.HomeScreen
import com.example.moodmusic.ui.screens.mood.MoodSelectionScreen
import com.example.moodmusic.ui.screens.songs.SongsScreen
import com.example.moodmusic.ui.viewmodel.HistoryViewModel
import com.example.moodmusic.ui.viewmodel.factory.HistoryViewModelFactory
import com.example.moodmusic.ui.screens.player.PlayerScreen
import com.example.moodmusic.ui.viewmodel.SongsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.moodmusic.ui.viewmodel.factory.SongsViewModelFactory
import com.example.moodmusic.data.repository.PlaylistRepository

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        composable(Routes.Home.route) {
            HomeScreen(
                onStartClick = { navController.navigate(Routes.Mood.route) }
            )
        }

        composable(Routes.Mood.route) {
            val historyViewModel: HistoryViewModel = viewModel(
                factory = HistoryViewModelFactory(AppContainer.moodHistoryRepository)
            )

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
                onFavoritesClick = { navController.navigate(Routes.Favorites.route) },
                onHistoryClick = { navController.navigate(Routes.History.route) },
                onPlaylistsClick = { navController.navigate(Routes.Playlists.route)}
            )
        }

        composable(
            route = Routes.Songs.route,
            arguments = listOf(
                navArgument("query") { type = NavType.StringType },
                navArgument("emotionName") { type = NavType.StringType },
                navArgument("emoji") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val query = Uri.decode(backStackEntry.arguments?.getString("query") ?: "happy music")
            val emotionName = Uri.decode(backStackEntry.arguments?.getString("emotionName") ?: "Alegría")
            val emoji = Uri.decode(backStackEntry.arguments?.getString("emoji") ?: "😄")
            val mood = MoodType.valueOf(
                backStackEntry.arguments?.getString("mood") ?: "HAPPY"
            )

            SongsScreen(
                query = query,
                emotionName = emotionName,
                emoji = emoji,
                mood = mood,
                onBackClick = { navController.popBackStack() },
                navController = navController
            )
        }

        composable(Routes.Favorites.route) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.History.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.Playlists.route) {
            PlaylistsScreen(
                onBackClick = { navController.popBackStack() },
                onPlaylistClick = { playlist ->
                    navController.navigate(
                        Routes.PlaylistDetail.createRoute(
                            playlist.id
                        )
                    )
                }
            )
        }
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
        composable(
            route = Routes.Player.route,
            arguments = listOf(
                navArgument("index") { type = NavType.IntType },
                navArgument("mood") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val mood = MoodType.valueOf(
                backStackEntry.arguments?.getString("mood") ?: "HAPPY"
            )

            val songsViewModel: SongsViewModel = viewModel(
                factory = SongsViewModelFactory(
                    AppContainer.musicRepository,
                    AppContainer.playlistRepository
                )
            )

            val songs by songsViewModel.currentPlaylist.collectAsState()

            PlayerScreen(
                songs = songs,
                startIndex = index,
                mood = mood,
                navController = navController
            )
        }
    }
}