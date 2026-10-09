import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoNotDisturbOn
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopChatBar(
    onDndChanged: (Boolean) -> Unit
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
            .statusBarsPadding() // Ensures top bar stays below camera notch
            .padding(horizontal = 16.dp, vertical = 8.dp)
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

            // Focus / DND Mode Button
            IconButton(
                onClick = {
                    isDndEnabled = !isDndEnabled
                    onDndChanged(isDndEnabled)
                    
                    // Optional: Direct user to system DND settings if needed
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
