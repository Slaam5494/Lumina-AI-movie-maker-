package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.ModuleType
import com.example.ui.components.CoinStoreModal
import com.example.ui.components.LuminaBottomNav
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FriendsHubScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MediaGenScreen
import com.example.ui.screens.MovieMakerScreen
import com.example.ui.screens.PromptStudioScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.LuminaGold
import com.example.ui.theme.LuminaTextSecondary
import com.example.ui.theme.LuminaTheme
import com.example.ui.viewmodel.LuminaViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LuminaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LuminaApp(onExitApp = { finish() })
                }
            }
        }
    }
}

@Composable
fun LuminaApp(
    onExitApp: () -> Unit,
    viewModel: LuminaViewModel = viewModel()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val currentModule by viewModel.currentModule.collectAsStateWithLifecycle()
    val userAccount by viewModel.userAccount.collectAsStateWithLifecycle()
    val isCoinStoreOpen by viewModel.isCoinStoreOpen.collectAsStateWithLifecycle()
    val isAdPlaying by viewModel.isAdPlaying.collectAsStateWithLifecycle()
    val adCountdown by viewModel.adCountdown.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    var lastBackPressTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Safe Back Navigation
    BackHandler {
        if (!isLoggedIn) {
            onExitApp()
        } else if (currentModule != ModuleType.DASHBOARD) {
            viewModel.navigateTo(ModuleType.DASHBOARD)
        } else {
            val now = System.currentTimeMillis()
            if (now - lastBackPressTime < 2000L) {
                onExitApp()
            } else {
                lastBackPressTime = now
                Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (!isLoggedIn) {
            LoginScreen(
                onLoginWithGoogle = { viewModel.loginWithGoogle() },
                onContinueAsGuest = { viewModel.loginAsGuest() }
            )
        } else {
            Scaffold(
                bottomBar = {
                    LuminaBottomNav(
                        currentModule = currentModule,
                        onSelectModule = { viewModel.navigateTo(it) }
                    )
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentModule) {
                        ModuleType.DASHBOARD -> {
                            DashboardScreen(
                                coinBalance = userAccount.coins,
                                onOpenStore = { viewModel.openCoinStore() },
                                onModuleSelected = { viewModel.navigateTo(it) },
                                onWatchAd = { viewModel.watchRewardedAd() }
                            )
                        }
                        ModuleType.MOVIE_MAKER -> {
                            val movieProjects by viewModel.movieProjects.collectAsStateWithLifecycle()
                            val isGenerating by viewModel.isMovieGenerating.collectAsStateWithLifecycle()
                            val progress by viewModel.movieGenProgress.collectAsStateWithLifecycle()
                            MovieMakerScreen(
                                movieProjects = movieProjects,
                                isGenerating = isGenerating,
                                progress = progress,
                                userCoins = userAccount.coins,
                                onBack = { viewModel.navigateTo(ModuleType.DASHBOARD) },
                                onBuildMovie = { script, duration: Double, bgm ->
                                    viewModel.buildMovieProject(script, duration.toInt(), bgm)
                                }
                            )
                        }
                        ModuleType.MEDIA_GEN -> {
                            val videoList by viewModel.videoList.collectAsStateWithLifecycle()
                            val imageList by viewModel.imageList.collectAsStateWithLifecycle()
                            val isVideoGenerating by viewModel.isVideoGenerating.collectAsStateWithLifecycle()
                            val isImageGenerating by viewModel.isImageGenerating.collectAsStateWithLifecycle()
                            MediaGenScreen(
                                videoList = videoList,
                                imageList = imageList,
                                isVideoGenerating = isVideoGenerating,
                                isImageGenerating = isImageGenerating,
                                userCoins = userAccount.coins,
                                onBack = { viewModel.navigateTo(ModuleType.DASHBOARD) },
                                onGenerateVideo = { prompt, style, ratio, duration: Int, motion ->
                                    viewModel.generateVideo(prompt, style, ratio, duration, motion)
                                },
                                onGenerateImage = { prompt, style, ratio ->
                                    viewModel.generateImage(prompt, style, ratio)
                                }
                            )
                        }
                        ModuleType.AI_CHAT -> {
                            val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
                            val isGenerating by viewModel.isChatGenerating.collectAsStateWithLifecycle()
                            AiChatScreen(
                                messages = messages,
                                isGenerating = isGenerating,
                                onBack = { viewModel.navigateTo(ModuleType.DASHBOARD) },
                                onSendMessage = { text, model ->
                                    viewModel.sendChatMessage(text, model)
                                },
                                onClearChat = { viewModel.clearChat() }
                            )
                        }
                        ModuleType.PROMPT_STUDIO -> {
                            val templates by viewModel.promptTemplates.collectAsStateWithLifecycle()
                            PromptStudioScreen(
                                templates = templates,
                                onBack = { viewModel.navigateTo(ModuleType.DASHBOARD) },
                                onNavigateTo = { viewModel.navigateTo(it) },
                                onCopyPrompt = {
                                    clipboardManager.setText(AnnotatedString(it))
                                    Toast.makeText(context, "Prompt copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                        ModuleType.FRIENDS_HUB -> {
                            val friends by viewModel.friends.collectAsStateWithLifecycle()
                            val statuses by viewModel.statuses.collectAsStateWithLifecycle()
                            val activeCallFriend by viewModel.activeCallFriend.collectAsStateWithLifecycle()
                            val isVideoCall by viewModel.isVideoCall.collectAsStateWithLifecycle()
                            val callSeconds by viewModel.callSeconds.collectAsStateWithLifecycle()
                            val isMuted by viewModel.isMuted.collectAsStateWithLifecycle()
                            val isSpeakerOn by viewModel.isSpeakerOn.collectAsStateWithLifecycle()
                            val activeChatFriend by viewModel.activeChatFriend.collectAsStateWithLifecycle()
                            val directMessages by viewModel.directMessages.collectAsStateWithLifecycle()
                            FriendsHubScreen(
                                friends = friends,
                                statuses = statuses,
                                activeCallFriend = activeCallFriend,
                                isVideoCall = isVideoCall,
                                callSeconds = callSeconds,
                                isMuted = isMuted,
                                isSpeakerOn = isSpeakerOn,
                                activeChatFriend = activeChatFriend,
                                directMessages = directMessages,
                                onBack = { viewModel.navigateTo(ModuleType.DASHBOARD) },
                                onOpenStore = { viewModel.openCoinStore() },
                                onStartCall = { friend, isVideo -> viewModel.startCall(friend, isVideo) },
                                onEndCall = { viewModel.endCall() },
                                onToggleMute = { viewModel.toggleMute() },
                                onToggleSpeaker = { viewModel.toggleSpeaker() },
                                onOpenChat = { viewModel.openFriendChat(it) },
                                onCloseChat = { viewModel.closeFriendChat() },
                                onSendDirectMessage = { viewModel.sendDirectMessage(it) },
                                onSaveStatus = { viewModel.saveStatus(it) },
                                onOpenWhatsApp = { ctx, phone -> viewModel.openWhatsAppChat(ctx, phone) },
                                onWatchAd = { viewModel.watchRewardedAd() }
                            )
                        }
                        ModuleType.SETTINGS -> {
                            SettingsScreen(
                                userAccount = userAccount,
                                onBack = { viewModel.navigateTo(ModuleType.DASHBOARD) },
                                onOpenStore = { viewModel.openCoinStore() },
                                onLogout = { viewModel.logout() },
                                onOpenWhatsApp = { ctx, phone -> viewModel.openWhatsAppChat(ctx, phone) },
                                onShowFeedbackToast = {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }

        // Coin Store Modal
        if (isCoinStoreOpen) {
            CoinStoreModal(
                currentBalance = userAccount.coins,
                adsWatchedToday = userAccount.adsWatchedToday,
                packages = viewModel.coinPackages,
                onBuyPackage = { method, pkgId -> viewModel.processPayment(method, pkgId) },
                onWatchRewardedAd = { viewModel.watchRewardedAd() },
                onClose = { viewModel.closeCoinStore() }
            )
        }

        // Rewarded Ad Simulation Overlay
        if (isAdPlaying) {
            RewardedAdOverlay(countdown = adCountdown)
        }
    }
}

@Composable
fun RewardedAdOverlay(
    countdown: Int,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = {}) {
        Box(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF010308))
                .border(BorderStroke(2.dp, LuminaGold), RoundedCornerShape(16.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = LuminaGold,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = "REWARDED SPONSOR AD",
                    color = LuminaGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Simulating AdMob High-Value Video Impression...",
                    color = LuminaTextSecondary,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
                CircularProgressIndicator(
                    color = LuminaGold,
                    trackColor = Color(0xFF1E2028),
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "Reward unlocks in: ${countdown}s",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
