package com.example.moodmusic.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.ui.screens.favorites.FavoritesScreen
import com.example.moodmusic.ui.screens.history.HistoryScreen
import com.example.moodmusic.ui.screens.home.HomeScreen
import com.example.moodmusic.ui.screens.mood.MoodSelectionScreen
import com.example.moodmusic.ui.screens.songs.SongsScreen
import com.example.moodmusic.ui.viewmodel.HistoryViewModel
import com.example.moodmusic.ui.viewmodel.factory.HistoryViewModelFactory

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
                    historyViewModel.addMood("${emotion.emoji} ${emotion.text}")
                    navController.navigate(
                        Routes.Songs.createRoute(
                            query = emotion.query,
                            emotionName = emotion.text,
                            emoji = emotion.emoji
                        )
                    )
                },
                onFavoritesClick = { navController.navigate(Routes.Favorites.route) },
                onHistoryClick = { navController.navigate(Routes.History.route) }
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

            SongsScreen(
                query = query,
                emotionName = emotionName,
                emoji = emoji,
                onBackClick = { navController.popBackStack() }
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
    }
}