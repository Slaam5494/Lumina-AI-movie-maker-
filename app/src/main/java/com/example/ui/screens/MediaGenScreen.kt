package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val CyberBackgroundMedia = Color(0xFF0D0F14)
val CyberSurfaceMedia = Color(0xFF161922)
val CyberAccentMedia = Color(0xFF00E5FF)
val CyberTextSecondaryMedia = Color(0xFF8A94A6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaGenScreen(
    videoList: List<Any> = emptyList(),
    imageList: List<Any> = emptyList(),
    isVideoGenerating: Boolean = false,
    isImageGenerating: Boolean = false,
    userCoins: Int = 0,
    onBack: () -> Unit = {},
    onGenerateVideo: (String, String, String, Any, String) -> Unit = { _, _, _, _, _ -> },
    onGenerateImage: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var promptText by remember { mutableStateOf("") }
    var showMenuForItem by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Media Generation Hub", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Upload Reference Media */ }) {
                        Icon(
                            imageVector = Icons.Default.UploadFile,
                            contentDescription = "Upload Reference",
                            tint = CyberAccentMedia
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberBackgroundMedia)
            )
        },
        containerColor = CyberBackgroundMedia
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (isVideoGenerating || isImageGenerating) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    color = CyberAccentMedia,
                    trackColor = CyberSurfaceMedia
                )
            }

            OutlinedTextField(
                value = promptText,
                onValueChange = { promptText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter prompt for Image or Video...", color = CyberTextSecondaryMedia) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberAccentMedia,
                    unfocusedBorderColor = CyberSurfaceMedia,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (promptText.isNotBlank()) {
                            onGenerateImage(promptText, "Cyber", "16:9")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberAccentMedia)
                ) {
                    Text("Generate Image", color = Color.Black)
                }

                Button(
                    onClick = {
                        if (promptText.isNotBlank()) {
                            onGenerateVideo(promptText, "Cinematic", "16:9", 5, "Smooth")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceMedia)
                ) {
                    Text("Generate Video", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Generated Assets (Long Press for Menu):", color = CyberTextSecondaryMedia)

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(imageList + videoList) { item ->
                    MediaCardItem(
                        itemTitle = item.toString(),
                        onDownload = { },
                        onLongPress = { showMenuForItem = item.toString() }
                    )
                }
            }
        }
    }

    showMenuForItem?.let { _ ->
        AlertDialog(
            onDismissRequest = { showMenuForItem = null },
            confirmButton = {
                TextButton(onClick = { showMenuForItem = null }) {
                    Text("Close", color = CyberAccentMedia)
                }
            },
            title = { Text("Asset Options", color = Color.White) },
            text = {
                Column {
                    TextButton(onClick = { showMenuForItem = null }) {
                        Text("Download Asset", color = Color.White)
                    }
                    TextButton(onClick = { showMenuForItem = null }) {
                        Text("Share Asset", color = Color.White)
                    }
                    TextButton(onClick = { showMenuForItem = null }) {
                        Text("Delete Asset", color = Color.Red)
                    }
                }
            },
            containerColor = CyberSurfaceMedia
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaCardItem(
    itemTitle: String,
    onDownload: () -> Unit,
    onLongPress: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CyberSurfaceMedia)
            .combinedClickable(
                onClick = {},
                onLongClick = onLongPress
            )
            .padding(8.dp)
    ) {
        Text(
            text = itemTitle,
            color = Color.White,
            modifier = Modifier.align(Alignment.TopStart)
        )

        IconButton(
            onClick = onDownload,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download",
                tint = CyberAccentMedia
            )
        }
    }
}
