package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.FabricationScreen
import com.example.ui.screens.ImageGenScreen
import com.example.ui.screens.VideoGenScreen
import com.example.ui.theme.LuminaTheme
import com.example.ui.theme.LuminaBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LuminaTheme(darkTheme = true, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LuminaBackground
                ) {
                    // Simple Navigation State for Lumina AI Suite
                    var currentScreen by remember { mutableStateOf("dashboard") }
                    var userCoins by remember { mutableDoubleStateOf(100.0) }

                    when (currentScreen) {
                        "dashboard" -> {
                            // You can replace this with your main Dashboard screen or Fabrication Hub
                            FabricationScreen(
                                onBack = { finish() }
                            )
                        }
                        "image_gen" -> {
                            ImageGenScreen(
                                imageList = emptyList(),
                                isGenerating = false,
                                userCoins = userCoins,
                                onBack = { currentScreen = "dashboard" },
                                onGenerate = { prompt, style, ratio ->
                                    // Handle Generation logic
                                }
                            )
                        }
                        "video_gen" -> {
                            VideoGenScreen(
                                videoList = emptyList(),
                                isGenerating = false,
                                progress = 0f,
                                userCoins = userCoins,
                                onBack = { currentScreen = "dashboard" },
                                onGenerate = { prompt, style, ratio, duration, motion ->
                                    // Handle Video Generation logic
                                }
                            )
                        }
                        else -> {
                            FabricationScreen(
                                onBack = { finish() }
                            )
                        }
                    }
                }
            }
        }
    }
}
