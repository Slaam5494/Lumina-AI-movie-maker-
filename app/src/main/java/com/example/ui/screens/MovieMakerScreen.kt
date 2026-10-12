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
                // مثال کے طور پر جہاں مووی میکر اور میڈیا جن اسکرین کال ہو رہی ہیں:
                
                // --- MovieMakerScreen Call ---
                MovieMakerScreen(
                    movieProjects = emptyList(),
                    isGenerating = false,
                    progress = 0f,
                    userCoins = 100,
                    onBack = { finish() },
                    onBuildMovie = { script, duration, bgm ->
                        // یہاں duration کو محفوظ طریقے سے Int میں کاسٹ کیا گیا ہے
                        val safeDuration = (duration as? Number)?.toInt() ?: duration.toString().toDoubleOrNull()?.toInt() ?: 10
                        // آگے کا لاجक جو آپ کا ماڈیول استعمال کرتا ہے
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
