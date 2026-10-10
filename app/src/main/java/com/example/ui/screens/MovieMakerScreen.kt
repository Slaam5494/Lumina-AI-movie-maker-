package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val CyberBackgroundMovie = Color(0xFF0D0F14)
val CyberSurfaceMovie = Color(0xFF161922)
val CyberAccentMovie = Color(0xFF00E5FF)
val CyberTextSecondaryMovie = Color(0xFF8A94A6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieMakerScreen(
    movieProjects: List<Any> = emptyList(),
    isGenerating: Boolean = false,
    progress: Float = 0f,
    userCoins: Int = 0,
    onBack: () -> Unit = {},
    // Yahan duration ko Double kar diya gaya hai taake error khatam ho jaye
    onBuildMovie: (script: String, duration: Double, bgm: String) -> Unit = { _, _, _ -> }
) {
    var scriptText by remember { mutableStateOf("") }
    var bgmText by remember { mutableStateOf("Default BGM") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Movie Studio", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                onValueChange = { scriptText = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("Enter movie script or scene description...", color = CyberTextSecondaryMovie) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberAccentMovie,
                    unfocusedBorderColor = CyberSurfaceMovie,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (scriptText.isNotBlank()) {
                        onBuildMovie(scriptText, 60.0, bgmText)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CyberAccentMovie)
            ) {
                Icon(Icons.Default.Movie, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Build AI Movie Project", color = Color.Black)
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
