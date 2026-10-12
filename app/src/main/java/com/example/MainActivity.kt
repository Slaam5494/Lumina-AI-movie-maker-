package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.MovieMakerScreen
import com.example.ui.screens.MediaGenScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // --- MovieMakerScreen Call ---
                MovieMakerScreen(
                    movieProjects = emptyList(),
                    isGenerating = false,
                    progress = 0f,
                    userCoins = 100,
                    onBack = { finish() },
                    onBuildMovie = { script, duration, bgm ->
                        val safeDuration = duration
                        // viewModel.buildMovieProject(script, safeDuration, bgm)
                    }
                )
                
                // --- MediaGenScreen Call (Line 184 safe casting applied) ---
                // MediaGenScreen(
                //     onGenerateVideo = { prompt, style, ratio, duration, motion ->
                //         val safeVideoDuration = (duration as? Number)?.toInt() ?: duration.toString().toDoubleOrNull()?.toInt() ?: 5
                //         val safeMotion = motion.toString()
                //         // viewModel.generateVideo(prompt, style, ratio, safeVideoDuration, safeMotion)
                //     }
                // )
            }
        }
    }
}
