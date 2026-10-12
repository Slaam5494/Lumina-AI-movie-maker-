package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val CyberBackgroundMovie = Color(0xFF0D0F14)
val CyberSurfaceMovie = Color(0xFF161922)
val CyberAccentMovie = Color(0xFF00E5FF)
val CyberTextSecondaryMovie = Color(0xFF8A94A6)
val CyberCoinGold = Color(0xFFFFD700)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieMakerScreen(
    movieProjects: List<Any> = emptyList(),
    isGenerating: Boolean = false,
    progress: Float = 0f,
    userCoins: Int = 0,
    hasUsedDailyFreeVideo: Boolean = false,
    onBack: () -> Unit = {},
    onBuildMovie: (script: String, duration: Int, bgm: String, requiredCoins: Int) -> Unit = { _, _, _, _ -> }
) {
    var scriptText by remember { mutableStateOf("") }
    var bgmText by remember { mutableStateOf("Default BGM") }
    var selectedDuration by remember { mutableIntStateOf(10) } // Default 10 Seconds
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 5 सेकंड फ्री लिमिट (روزانہ 1 بار)، یا ہر 5 سیکنڈ کے 2 Coins
    val calculatedCoinsNeeded = remember(selectedDuration, hasUsedDailyFreeVideo) {
        if (!hasUsedDailyFreeVideo && selectedDuration <= 10) {
            0 // DAILY FREE (5-10 SECONDS)
        } else {
            val paidDuration = if (!hasUsedDailyFreeVideo) selectedDuration - 10 else selectedDuration
            if (paidDuration <= 0) 0 else ((paidDuration + 4) / 5) * 2
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Movie Studio", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = "Coins", tint = CyberCoinGold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$userCoins", color = CyberCoinGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberBackgroundMovie)
            )
        },
        containerColor = CyberBackgroundMovie
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (isGenerating) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    color = CyberAccentMovie,
                    trackColor = CyberSurfaceMovie
                )
            }

            OutlinedTextField(
                value = scriptText,
                onValueChange = { 
                    scriptText = it 
                    errorMessage = null
                },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                placeholder = { Text("Enter movie script or scene description...", color = CyberTextSecondaryMovie) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberAccentMovie,
                    unfocusedBorderColor = CyberSurfaceMovie,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Duration Selection
            Text("Select Duration (Seconds):", color = CyberTextSecondaryMovie, fontSize = 12.sp)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(5, 10, 15, 30).forEach { duration ->
                    FilterChip(
                        selected = selectedDuration == duration,
                        onClick = { selectedDuration = duration },
                        label = { Text("${duration}s", color = Color.White) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyberAccentMovie.copy(alpha = 0.3f),
                            containerColor = CyberSurfaceMovie
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cost Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberSurfaceMovie)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (!hasUsedDailyFreeVideo && selectedDuration <= 10) "Daily Offer: FREE" else "Required Coins:",
                        color = if (!hasUsedDailyFreeVideo && selectedDuration <= 10) Color.Green else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = if (calculatedCoinsNeeded == 0) "FREE" else "$calculatedCoinsNeeded Coins",
                        color = CyberCoinGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (scriptText.isBlank()) {
                        errorMessage = "Please enter a script first!"
                    } else if (userCoins < calculatedCoinsNeeded) {
                        errorMessage = "Insufficient coins! You need $calculatedCoinsNeeded coins."
                    } else {
                        errorMessage = null
                        onBuildMovie(scriptText, selectedDuration, bgmText, calculatedCoinsNeeded)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CyberAccentMovie)
            ) {
                Icon(Icons.Default.Movie, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Build AI Movie Project", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Your Movie Projects:", color = CyberTextSecondaryMovie)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movieProjects) { project ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CyberSurfaceMovie)
                    ) {
                        Text(
                            text = project.toString(),
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
