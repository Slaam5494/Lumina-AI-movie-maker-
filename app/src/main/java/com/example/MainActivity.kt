package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ImageGenScreen
import com.example.ui.screens.MovieMakerScreen
import com.example.ui.screens.VideoGenScreen
import com.example.ui.screens.FriendsHubScreen
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
                    var remainingFreeImages by remember { mutableIntStateOf(3) }

                    // Safety handling so app never crashes on navigation mismatch
                    when (currentScreen) {
                        "dashboard" -> {
                            DashboardScreen(
                                coinBalance = userCoins.toDouble(),
                                onOpenStore = { /* Store logic */ },
                                onModuleSelected = { module ->
                                    currentScreen = when (module) {
                                        ModuleType.MOVIE_MAKER -> "movie_maker"
                                        ModuleType.MEDIA_GEN -> "video_gen"
                                        ModuleType.AI_CHAT -> "dashboard"
                                        ModuleType.PROMPT_STUDIO -> "dashboard"
                                        ModuleType.FRIENDS_HUB -> "friends_hub"
                                        ModuleType.SETTINGS -> "dashboard"
                                        ModuleType.DASHBOARD -> "dashboard"
                                    }
                                },
                                onWatchAd = {
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
                                    // 5s free video logic
                                },
                                onRedirectToMovieMaker = { script ->
                                    currentScreen = "movie_maker"
                                }
                            )
                        }
                         "friends_hub" -> {
                            FriendsHubScreen(
                             coinBalance = userCoins,
                             onBack = { currentScreen = "dashboard" },
                             onAddFriend = { userCoins += 3 },
                             onShareApp = { userCoins += 5 },
                             onOpenWhatsApp = { /* WhatsApp open logic */ },
                            onWatchAd = { userCoins += 10 }
                        )
                         }

                        else -> {
                            DashboardScreen(
                                coinBalance = userCoins.toDouble(),
                                onOpenStore = {},
                                onModuleSelected = { currentScreen = "dashboard" },
                                onWatchAd = {}
                            )
                        }
                    }
                }
            }
        }
    }
}
