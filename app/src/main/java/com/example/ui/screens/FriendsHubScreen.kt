package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.DirectMessage
import com.example.data.model.FriendItem
import com.example.data.model.StatusItem
import com.example.ui.theme.LuminaBackground
import com.example.ui.theme.LuminaBorder
import com.example.ui.theme.LuminaCardBg
import com.example.ui.theme.LuminaCyan
import com.example.ui.theme.LuminaGold
import com.example.ui.theme.LuminaGreen
import com.example.ui.theme.LuminaPink
import com.example.ui.theme.LuminaPurple
import com.example.ui.theme.LuminaRed
import com.example.ui.theme.LuminaSurface
import com.example.ui.theme.LuminaTextMuted
import com.example.ui.theme.LuminaTextPrimary
import com.example.ui.theme.LuminaTextSecondary

@Composable
fun FriendsHubScreen(
    friends: List<FriendItem> = emptyList(),
    statuses: List<StatusItem> = emptyList(),
    activeCallFriend: FriendItem? = null,
    isVideoCall: Boolean = false,
    callSeconds: Int = 0,
    isMuted: Boolean = false,
    isSpeakerOn: Boolean = false,
    activeChatFriend: FriendItem? = null,
    directMessages: List<DirectMessage> = emptyList(),
    userCoins: Int = 100,
    onBack: () -> Unit = {},
    onOpenStore: () -> Unit = {},
    onStartCall: (FriendItem, Boolean) -> Unit = { _, _ -> },
    onEndCall: () -> Unit = {},
    onToggleMute: () -> Unit = {},
    onToggleSpeaker: () -> Unit = {},
    onOpenChat: (FriendItem) -> Unit = {},
    onCloseChat: () -> Unit = {},
    onSendDirectMessage: (String) -> Unit = {},
    onSaveStatus: (String) -> Unit = {},
    onOpenWhatsApp: (Context, String) -> Unit = { _, _ -> },
    onWatchAd: () -> Unit = {},
    onAddFriend: (String) -> Unit = {},
    onShareApp: () -> Unit = {},
    modifier: Modifier = Modifier
) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = LuminaGreen
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "FRIENDS HUB",
                        color = LuminaGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Add Friends (+3 Coins) • Share (+5 Coins)",
                        color = LuminaTextMuted,
                        fontSize = 8.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(LuminaGold.copy(alpha = 0.15f))
                    .border(1.dp, LuminaGold, RoundedCornerShape(8.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "$userCoins Coins",
                    color = LuminaGold,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) LuminaGreen else LuminaSurface)
                        .border(1.dp, if (isSelected) LuminaGreen else LuminaBorder, RoundedCornerShape(6.dp))
                        .clickable { selectedTab = index }
                        .padding(vertical = 6.dp)
                        .testTag("friends_tab_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> ChatsTabContent(
                friends = friends,
                onOpenChat = onOpenChat,
                onStartCall = onStartCall,
                onOpenWhatsApp = { phone -> onOpenWhatsApp(context, phone) },
                onOpenStore = onOpenStore,
                onWatchAd = onWatchAd,
                onAddFriend = onAddFriend,
                onShareApp = onShareApp
            )
            1 -> StatusSaverTabContent(
                statuses = statuses,
                onSaveStatus = onSaveStatus,
                onWatchAd = onWatchAd
            )
            2 -> VipThemesTabContent(
                onOpenStore = onOpenStore
            )
        }
    }
}

@Composable
fun ChatsTabContent(
    friends: List<FriendItem>,
    onOpenChat: (FriendItem) -> Unit,
    onStartCall: (FriendItem, Boolean) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    onOpenStore: () -> Unit,
    onWatchAd: () -> Unit,
    onAddFriend: (String) -> Unit,
    onShareApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Add Friend & Share App Banner (Reward System)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(LuminaSurface)
                    .border(1.dp, LuminaGreen, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "ADD FRIEND (+3 COINS) OR SHARE APP (+5 COINS)",
                    color = LuminaGreen,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        placeholder = { Text("Enter Username / Phone", color = LuminaTextMuted, fontSize = 8.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LuminaGreen,
                            unfocusedBorderColor = LuminaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(LuminaGreen)
                            .clickable(enabled = searchQuery.isNotBlank()) {
                                onAddFriend(searchQuery)
                                searchQuery = ""
                            }
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "ADD", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Share Button for +5 Coins
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(LuminaCyan
