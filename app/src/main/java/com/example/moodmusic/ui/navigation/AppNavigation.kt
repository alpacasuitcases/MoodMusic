package com.example.moodmusic.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.ui.screens.favorites.FavoritesScreen
import com.example.moodmusic.ui.screens.history.HistoryScreen
import com.example.moodmusic.ui.screens.home.HomeScreen
import com.example.moodmusic.ui.screens.mood.MoodSelectionScreen
import com.example.moodmusic.ui.screens.songs.SongsScreen

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
            MoodSelectionScreen(
                onMoodSelected = { mood ->
                    navController.navigate(Routes.Songs.createRoute(mood.name))
                },
                onFavoritesClick = { navController.navigate(Routes.Favorites.route) },
                onHistoryClick = { navController.navigate(Routes.History.route) }
            )
        }

        composable(
            route = "songs/{mood}",
            arguments = listOf(navArgument("mood") { type = NavType.StringType })
        ) { backStackEntry ->
            val moodArg = backStackEntry.arguments?.getString("mood") ?: MoodType.HAPPY.name
            SongsScreen(
                mood = MoodType.valueOf(moodArg),
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