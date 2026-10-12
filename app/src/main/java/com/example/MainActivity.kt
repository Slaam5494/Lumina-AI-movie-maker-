package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FabricationScreen
import com.example.ui.screens.ImageGenScreen
import com.example.ui.screens.MovieMakerScreen
import com.example.ui.screens.VideoGenScreen
import com.example.ui.theme.LuminaTheme
import com.example.ui.theme.LuminaBackground
import com.example.data.model.ModuleType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LuminaTheme(darkTheme = true, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LuminaBackground
                ) {
                    var currentScreen by remember { mutableStateOf("dashboard") }
                    var userCoins by remember { mutableIntStateOf(100) }
                    var hasUsedDailyFreeVideo by remember { mutableStateOf(false) }
                    
                    // Daily Free Images State (روزانہ 3 تصاویر مفت)
                    var remainingFreeImages by remember { mutableIntStateOf(3) }

                    when (currentScreen) {
                        "dashboard" -> {
                            DashboardScreen(
                                coinBalance = userCoins.toDouble(),
                                onOpenStore = { /* Store/Coin shop logic */ },
                                onModuleSelected = { module ->
                                    when (module) {
                                        ModuleType.MOVIE_MAKER -> currentScreen = "movie_maker"
                                        ModuleType.MEDIA_GEN -> currentScreen = "video_gen" // یا image_gen
                                        ModuleType.AI_CHAT -> { /* AI Chat screen if available */ }
                                        ModuleType.PROMPT_STUDIO -> { /* Prompt Studio */ }
                                        ModuleType.FRIENDS_HUB -> currentScreen = "friends_hub"
                                        ModuleType.SETTINGS -> { /* Settings */ }
                                    }
                                },
                                onWatchAd = {
                                    // Ad watch reward logic (+ coins)
                                    userCoins += 10
                                }
                            )
                        }
                        "movie_maker" -> {
                            MovieMakerScreen(
                                movieProjects = emptyList(),
                                isGenerating = false,
                                progress = 0f,
                                userCoins = userCoins,
                                hasUsedDailyFreeVideo = hasUsedDailyFreeVideo,
                                onBack = { currentScreen = "dashboard" },
                                onBuildMovie = { script, duration, bgm, requiredCoins ->
                                    if (requiredCoins == 0) {
                                        hasUsedDailyFreeVideo = true
                                    } else if (userCoins >= requiredCoins) {
                                        userCoins -= requiredCoins
                                    }
                                }
                            )
                        }
                        "image_gen" -> {
                            ImageGenScreen(
                                imageList = emptyList(),
                                isGenerating = false,
                                userCoins = userCoins.toDouble(),
                                remainingFreeImages = remainingFreeImages,
                                onBack = { currentScreen = "dashboard" },
                                onGenerate = { prompt, style, ratio, requiredCoins ->
                                    if (requiredCoins == 0) {
                                        if (remainingFreeImages > 0) {
                                            remainingFreeImages--
                                        }
                                    } else if (userCoins >= requiredCoins) {
                                        userCoins -= requiredCoins
                                    }
                                }
                            )
                        }
                        "video_gen" -> {
                            VideoGenScreen(
                                videoList = emptyList(),
                                isGenerating = false,
                                progress = 0f,
                                userCoins = userCoins.toDouble(),
                                onBack = { currentScreen = "dashboard" },
                                onGenerate = { prompt, style, ratio, duration, motion ->
                                    // 5 سیکنڈ یا اس سے کم کی فری ویڈیو کا لاجک
                                },
                                onRedirectToMovieMaker = { script ->
                                    // 5 سیکنڈ سے بڑی ویڈیو کے لیے مووی میکر اسکرین پر ری ڈائریکٹ کریں
                                    currentScreen = "movie_maker"
                                }
                            )
                        }
                        else -> {
                            DashboardScreen(
                                coinBalance = userCoins.toDouble(),
                                onOpenStore = {},
                                onModuleSelected = { module ->
                                    if (module == ModuleType.MOVIE_MAKER) currentScreen = "movie_maker"
                                },
                                onWatchAd = {}
                            )
                        }
                    }
                }
            }
        }
    }
}
