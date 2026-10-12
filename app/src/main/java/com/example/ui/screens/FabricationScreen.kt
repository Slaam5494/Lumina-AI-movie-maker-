package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun FabricationScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTool by remember { mutableStateOf("artape") }
    var tapeDistance by remember { mutableStateOf("1185 mm (3.88 ft)") }
    var itemCount by remember { mutableStateOf(24) }
    var isBalanced by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LuminaBackground)
            .padding(6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LuminaSurface.copy(alpha = 0.8f))
                .border(1.dp, LuminaBorder, RoundedCornerShape(6.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = LuminaCyan.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = LuminaCyan, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "BACK", color = LuminaCyan, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = when (selectedTool) {
                    "artape" -> "12M AR TAPE MEASURE"
                    "aicounter" -> "AI MATERIAL COUNTER"
                    "level" -> "360° SPIRIT LEVEL"
                    else -> "AR RIGHT ANGLE (90°)"
                },
                color = LuminaCyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }

        // Top Ad Banner Slot
        AdBannerBox(label = "TOP AD BANNER")

        // Main Viewport Box (AR View / Level View)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(LuminaSurfaceVariant.copy(alpha = 0.5f))
                .border(1.dp, LuminaBorder, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTool) {
                "artape" -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(6.dp)
                                .background(Brush.horizontalGradient(listOf(LuminaCyan, LuminaPurple, LuminaCyan)), RoundedCornerShape(3.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                color = LuminaSurface,
                                border = BorderStroke(1.dp, LuminaCyan),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = tapeDistance,
                                    color = LuminaCyan,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "MEASURED RANGE: 1185mm / 1200mm\nSTATUS: LONG DISTANCE AR LOCKED",
                            color = LuminaCyan,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                "aicounter" -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Surface(
                            color = LuminaSurface,
                            border = BorderStroke(1.dp, LuminaPurple),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "AI MATERIAL COUNT", color = LuminaPurple, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Text(text = "TOTAL PIECES: $itemCount", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
                "level" -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(if (isBalanced) LuminaGreen.copy(alpha = 0.2f) else LuminaCyan.copy(alpha = 0.1f), RoundedCornerShape(50.dp))
                                .border(2.dp, if (isBalanced) LuminaGreen else LuminaCyan, RoundedCornerShape(50.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(if (isBalanced) LuminaGreen else LuminaCyan, RoundedCornerShape(8.dp))
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isBalanced) "PERFECTLY BALANCED (0.0°)" else "ADJUST ALIGNMENT",
                            color = if (isBalanced) LuminaGreen else LuminaCyan,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                "rightangle" -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "AR RIGHT ANGLE (90.0°)", color = LuminaCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Diagonal Scale: 1414mm locked", color = LuminaTextSecondary, fontSize = 7.sp)
                        }
                    }
                }
            }
        }

        // Mode Switching Bottom Control Panel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LuminaSurface)
                .border(1.dp, LuminaBorder, RoundedCornerShape(6.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FabricationToolButton(
                title = "AR TAPE",
                icon = Icons.Default.Straighten,
                isActive = selectedTool == "artape",
                onClick = { selectedTool = "artape" }
            )
            FabricationToolButton(
                title = "AI COUNTER",
                icon = Icons.Default.Dashboard,
                isActive = selectedTool == "aicounter",
                onClick = { selectedTool = "aicounter" }
            )
            FabricationToolButton(
                title = "360° LEVEL",
                icon = Icons.Default.Explore,
                isActive = selectedTool == "level",
                onClick = { selectedTool = "level" }
            )
            FabricationToolButton(
                title = "90° ANGLE",
                icon = Icons.Default.Architecture,
                isActive = selectedTool == "rightangle",
                onClick = { selectedTool = "rightangle" }
            )
