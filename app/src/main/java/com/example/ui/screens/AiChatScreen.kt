package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.DoNotDisturbOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.provider.Settings

// Cyber Dark Palette Colors
val CyberBackground = Color(0xFF0D0F14)
val CyberSurface = Color(0xFF161922)
val CyberAccent = Color(0xFF00E5FF) // Neon Cyan
val CyberTextSecondary = Color(0xFF8A94A6)

data class ChatMessage(val text: String, val isUser: Boolean)

@Composable
fun AiChatScreen(
    modifier: Modifier = Modifier,
    uiState: Any? = null,
    onClearChat: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onSendMessage: (String) -> Unit = {},
    onDndToggle: (Boolean) -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
    ) {
        // Top Bar with Status Bar Padding, DND Toggle & Clear Chat Action
        TopChatBar(
            onDndChanged = { isEnabled ->
                onDndToggle(isEnabled)
            },
            onClearChat = {
                messages.clear()
                onClearChat()
            }
        )

        // Chat Message List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            reverseLayout = false
        ) {
            items(messages) { message ->
                ChatMessageBubble(message = message)
            }
        }

        // Bottom Input Area
        ChatInputArea(
            text = messageText,
            onTextChange = { messageText = it },
            onSend = {
                if (messageText.isNotBlank()) {
                    val userText = messageText
                    messages.add(ChatMessage(userText, isUser = true))
                    onSendMessage(userText)
                    messages.add(ChatMessage("Lumina AI: Processing '$userText'", isUser = false))
                    messageText = ""
                }
            }
        )
    }
}

@Composable
fun TopChatBar(
    onDndChanged: (Boolean) -> Unit,
    onClearChat: () -> Unit
) {
    var isDndEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CyberBackground,
                        CyberBackground.copy(alpha = 0.95f),
                        Color.Transparent
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lumina AI Chat",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Clear Chat Button
                IconButton(onClick = onClearChat) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear Chat",
                        tint = CyberTextSecondary
                    )
                }

                // Focus / DND Mode Button
                IconButton(
                    onClick = {
                        isDndEnabled = !isDndEnabled
                        onDndChanged(isDndEnabled)
                        if (isDndEnabled) {
                            try {
                                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isDndEnabled) Icons.Filled.DoNotDisturbOn else Icons.Outlined.DoNotDisturbOff,
                        contentDescription = "Focus DND Mode",
                        tint = if (isDndEnabled) CyberAccent else CyberTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            color = if (message.isUser) CyberAccent.copy(alpha = 0.2f) else CyberSurface,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = message.text,
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun ChatInputArea(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Ask Lumina AI...", color = CyberTextSecondary) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberAccent,
                unfocusedBorderColor = CyberSurface,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSend,
            modifier = Modifier.background(CyberAccent, shape = MaterialTheme.shapes.small)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.Black
            )
        }
    }
}
