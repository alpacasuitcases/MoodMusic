package com.example.moodmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.ui.navigation.AppNavigation
import com.example.moodmusic.ui.theme.MoodMusicTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AppContainer.init(applicationContext)

        setContent {
            MoodMusicTheme {
                AppNavigation()
            }
        }
    }
}