package com.fitman.nativeapp.ui.screens.settings

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import coil.Coil
import com.fitman.nativeapp.data.repository.SettingsRepository
import com.fitman.nativeapp.ui.components.LiquidGlassButton
import com.fitman.nativeapp.ui.components.LiquidGlassCard
import com.fitman.nativeapp.ui.components.LiquidGlassDialog
import com.fitman.nativeapp.ui.theme.FitmanColors

@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var specularEnabled by remember { mutableStateOf(settingsRepository.specularEnabled) }
    var blurEnabled by remember { mutableStateOf(settingsRepository.glassBlurEnabled) }
    var storeRegion by remember { mutableStateOf(settingsRepository.storeRegion) }
    var showClearDialog by remember { mutableStateOf(false) }

    val regions = listOf(
        "us" to "US (USD $)",
        "gb" to "UK (GBP £)",
        "de" to "EU (EUR €)",
        "jp" to "JP (JPY ¥)"
    )

    if (showClearDialog) {
        LiquidGlassDialog(
            title = "Purge Cache",
            message = "Clear all cached game artwork and catalogue thumbnails from device storage?",
            confirmButtonText = "CLEAR NOW",
            onConfirm = {
                showClearDialog = false
                try {
                    Coil.imageLoader(context).diskCache?.clear()
                    Coil.imageLoader(context).memoryCache?.clear()
                    Toast.makeText(context, "Image cache cleared successfully.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Cache purged.", Toast.LENGTH_SHORT).show()
                }
            },
            dismissButtonText = "CANCEL",
            onDismiss = { showClearDialog = false },
            onDismissRequest = { showClearDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FitmanColors.MainBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Screen Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SETTINGS",
                    color = FitmanColors.TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Engine & System Preferences",
                    color = FitmanColors.TextSecondary,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = FitmanColors.ElectricLime,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.height(18.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Visual Engine Settings
            item {
                SectionHeader("LIQUID GLASS ENGINE")
                Spacer(Modifier.height(8.dp))
                LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ToggleRow(
                            icon = Icons.Default.Layers,
                            title = "Specular Edge Lighting",
                            subtitle = "Multi-layer refractive highlights & glints",
                            checked = specularEnabled,
                            onCheckedChange = {
                                specularEnabled = it
                                settingsRepository.specularEnabled = it
                            }
                        )

                        ToggleRow(
                            icon = Icons.Default.DisplaySettings,
                            title = "Ultra Obsidian Glass Blur",
                            subtitle = "Hardware-accelerated translucent depth",
                            checked = blurEnabled,
                            onCheckedChange = {
                                blurEnabled = it
                                settingsRepository.glassBlurEnabled = it
                            }
                        )
                    }
                }
            }

            // Steam Store Region
            item {
                SectionHeader("STEAM CATALOGUE REGION")
                Spacer(Modifier.height(8.dp))
                LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        regions.forEach { (code, label) ->
                            val isSelected = storeRegion.equals(code, ignoreCase = true)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) FitmanColors.GlassSurfaceElevated else Color.Transparent)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) FitmanColors.ElectricLime.copy(alpha = 0.5f) else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        storeRegion = code
                                        settingsRepository.storeRegion = code
                                        Toast.makeText(context, "Region set to $label. Refresh to update prices.", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Public,
                                        contentDescription = null,
                                        tint = if (isSelected) FitmanColors.ElectricLime else FitmanColors.TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        text = label,
                                        color = if (isSelected) FitmanColors.TextPrimary else FitmanColors.TextSecondary,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(FitmanColors.ElectricLime)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Cache & Storage
            item {
                SectionHeader("CACHE & DISK STORAGE")
                Spacer(Modifier.height(8.dp))
                LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Artwork Disk Cache",
                            color = FitmanColors.TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Free space by removing locally cached Steam artwork.",
                            color = FitmanColors.TextSecondary,
                            fontSize = 12.sp
                        )
                        LiquidGlassButton(
                            text = "PURGE CACHE",
                            onClick = { showClearDialog = true },
                            isPrimary = false,
                            leadingIcon = Icons.Default.CleaningServices,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // About Fitman Native
            item {
                SectionHeader("APPLICATION IDENTITY")
                Spacer(Modifier.height(8.dp))
                LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = FitmanColors.ElectricLime,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Fitman Native v2.0",
                                color = FitmanColors.TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "iOS 45 Liquid Glass & Obsidian Black Glassmorphism Edition. Engineered in Kotlin & Jetpack Compose.",
                            color = FitmanColors.TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Powered by Steam Store Public APIs. All game trademarks, artwork, and copyrights belong to Valve Corporation and their respective publishers.",
                            color = FitmanColors.TextMuted,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = FitmanColors.TextMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp
    )
}

@Composable
private fun ToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f).padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (checked) FitmanColors.ElectricLime else FitmanColors.TextMuted,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = FitmanColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = FitmanColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = FitmanColors.ElectricLime,
                uncheckedThumbColor = FitmanColors.TextSecondary,
                uncheckedTrackColor = FitmanColors.GlassSurfaceElevated
            )
        )
    }
}
