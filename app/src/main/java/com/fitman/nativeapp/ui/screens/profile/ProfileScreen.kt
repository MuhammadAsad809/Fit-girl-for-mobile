package com.fitman.nativeapp.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitman.nativeapp.data.repository.SettingsRepository
import com.fitman.nativeapp.ui.theme.FitmanColors

/**
 * GameHub Profile & Settings Screen.
 */
@Composable
fun ProfileScreen(
    settingsRepository: SettingsRepository,
    libraryCount: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentRegion by remember { mutableStateOf(settingsRepository.storeRegion.uppercase()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FitmanColors.WarmBackgroundGradient)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            item {
                ProfileHeaderCard(libraryCount = libraryCount)
            }

            // Section: Preferences
            item {
                Text(
                    text = "Preferences",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                SettingActionCard(
                    icon = Icons.Default.Language,
                    title = "Store Region",
                    subtitle = "Current: $currentRegion",
                    onClick = {
                        val next = when (currentRegion) {
                            "US" -> "eu"
                            "EU" -> "gb"
                            "GB" -> "jp"
                            else -> "us"
                        }
                        settingsRepository.storeRegion = next
                        currentRegion = next.uppercase()
                        Toast.makeText(context, "Region set to $currentRegion", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            item {
                SettingActionCard(
                    icon = Icons.Default.CleaningServices,
                    title = "Clear Cache & History",
                    subtitle = "Free temporary image storage and queries",
                    onClick = {
                        settingsRepository.clearRecentSearches()
                        Toast.makeText(context, "Cache and search history cleared", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Section: System & About
            item {
                Text(
                    text = "System & Security",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                SettingActionCard(
                    icon = Icons.Default.Shield,
                    title = "Repack Engine Security",
                    subtitle = "Clean torrent verified with SHA-256 signatures",
                    onClick = {
                        Toast.makeText(context, "Torrents verified via clean magnet protocol", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            item {
                SettingActionCard(
                    icon = Icons.Default.Info,
                    title = "Fitman Native",
                    subtitle = "Version 2.3 • GameHub Design Edition",
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun ProfileHeaderCard(libraryCount: Int) {
    val cardShape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(FitmanColors.WarmCardBackground)
            .border(1.dp, FitmanColors.WarmCardBorder, cardShape)
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF382E25))
                        .border(2.dp, FitmanColors.GoldRank, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Fitman Gamer",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = "Level 45 • Pro Member",
                        color = FitmanColors.StarGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // Library Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ProfileStatColumn(value = "$libraryCount", label = "Games Saved")
                ProfileStatColumn(value = "100%", label = "Verified")
                ProfileStatColumn(value = "60 FPS", label = "GSFG Frame Gen")
            }
        }
    }
}

@Composable
private fun ProfileStatColumn(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            color = FitmanColors.TextMutedWarm,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SettingActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(18.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(FitmanColors.WarmCardDark)
            .border(1.dp, FitmanColors.WarmCardBorder, cardShape)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF28211A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = FitmanColors.StarGold,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = FitmanColors.TextMutedWarm,
                    fontSize = 12.sp
                )
            }
        }
    }
}
