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
// دیگر تمام ضروری امپورٹس یہاں موجود ہیں

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // آپ کا مرکزی نیویگیشن یا اسکرین لاجک
                
                // --- MovieMakerScreen Call ---
                MovieMakerScreen(
                    movieProjects = emptyList(),
                    isGenerating = false,
                    progress = 0f,
                    userCoins = 100,
                    onBack = { finish() },
                    onBuildMovie = { script, duration, bgm ->
                        // یہاں duration اب براہ راست Int ہے، اس لیے اسے محفوظ طریقے سے استعمال کیا جا رہا ہے
                        val safeDuration = duration
                        // اگر آپ کا viewModel یہاں موجود ہے تو آپ اس طرح کال کر سکتے ہیں:
                        // viewModel.buildMovieProject(script, safeDuration, bgm)
                    }
                )
                
                // --- MediaGenScreen Call ---
                // onGenerateVideo = { prompt, style, ratio, duration, motion ->
                //     val safeVideoDuration = (duration as? Number)?.toInt() ?: duration.toString().toDoubleOrNull()?.toInt() ?: 5
                //     val safeMotion = motion.toString()
                //     viewModel.generateVideo(prompt, style, ratio, safeVideoDuration, safeMotion)
                // }
            }
        }
    }
}
