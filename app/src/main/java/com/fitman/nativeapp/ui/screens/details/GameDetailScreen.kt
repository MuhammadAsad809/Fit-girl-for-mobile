package com.fitman.nativeapp.ui.screens.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.fitman.nativeapp.data.model.GameDetail
import com.fitman.nativeapp.data.model.LibraryStatus
import com.fitman.nativeapp.data.model.Screenshot
import com.fitman.nativeapp.ui.components.LiquidGlassButton
import com.fitman.nativeapp.ui.components.LiquidGlassCard
import com.fitman.nativeapp.ui.components.LiquidGlassChip
import com.fitman.nativeapp.ui.components.LiquidGlassIconButton
import com.fitman.nativeapp.ui.theme.FitmanColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameDetailScreen(
    game: GameDetail,
    isFavorite: Boolean,
    currentStatus: LibraryStatus?,
    onToggleFavorite: () -> Unit,
    onStatusChange: (LibraryStatus) -> Unit,
    onBack: () -> Unit,
    openUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var selectedScreenshot by remember { mutableStateOf<Screenshot?>(null) }
    var showSpecs by remember { mutableStateOf(false) }
    var showStatusPicker by remember { mutableStateOf(false) }

    // Full-screen Image Lightbox Dialog
    if (selectedScreenshot != null) {
        Dialog(
            onDismissRequest = { selectedScreenshot = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
            ) {
                AsyncImage(
                    model = selectedScreenshot!!.full,
                    contentDescription = "Screenshot",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )

                LiquidGlassIconButton(
                    icon = Icons.Default.Close,
                    onClick = { selectedScreenshot = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(24.dp),
                    tint = Color.White
                )
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(FitmanColors.MainBackground),
        contentPadding = PaddingValues(bottom = 50.dp)
    ) {
        // Hero Artwork with Multi-stop Obsidian Fade
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = game.backgroundImage.ifBlank { game.image },
                    contentDescription = game.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Obsidian gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x99050608),
                                    Color.Transparent,
                                    Color(0xB3050608),
                                    FitmanColors.MainBackground
                                )
                            )
                        )
                )

                // Top Floating Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LiquidGlassIconButton(
                        icon = Icons.Default.ArrowBack,
                        onClick = onBack,
                        contentDescription = "Back"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LiquidGlassIconButton(
                            icon = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            onClick = onToggleFavorite,
                            tint = if (isFavorite) FitmanColors.ElectricLime else FitmanColors.TextPrimary,
                            contentDescription = "Favorite"
                        )
                    }
                }

                // Title & Metacritic Badge
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    if (game.metacriticScore != null && game.metacriticScore > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(FitmanColors.GlassSurfaceElevated)
                                .border(1.dp, FitmanColors.ElectricLime.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .clickable {
                                    if (!game.metacriticUrl.isNullOrBlank()) openUrl(game.metacriticUrl)
                                }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = FitmanColors.ElectricLime,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = "METACRITIC: ${game.metacriticScore}/100",
                                color = FitmanColors.ElectricLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                    }

                    Text(
                        text = game.title,
                        color = FitmanColors.TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 30.sp
                    )
                }
            }
        }

        // Platform & Controller Badges
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (game.platforms.windows) {
                    PlatformBadge("Windows PC")
                }
                if (game.platforms.mac) {
                    PlatformBadge("macOS")
                }
                if (game.platforms.linux) {
                    PlatformBadge("Steam Deck / Linux")
                }
                if (game.controllerSupport.isNotBlank()) {
                    PlatformBadge("Gamepad Ready", icon = Icons.Default.Gamepad)
                }
            }
        }

        // Action Buttons: Open Official Store & Library Status
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LiquidGlassButton(
                    text = "OPEN OFFICIAL STEAM STORE",
                    onClick = { openUrl(game.url) },
                    isPrimary = true,
                    trailingIcon = Icons.Default.OpenInNew,
                    modifier = Modifier.fillMaxWidth()
                )

                // Library Status Selector
                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showStatusPicker = !showStatusPicker }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = if (isFavorite) FitmanColors.ElectricLime else FitmanColors.TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "LIBRARY CATEGORY",
                                    color = FitmanColors.TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = currentStatus?.label ?: "Not in Library (Tap to Add)",
                                    color = if (isFavorite) FitmanColors.ElectricLime else FitmanColors.TextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Icon(
                            imageVector = if (showStatusPicker) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = FitmanColors.TextMuted
                        )
                    }
                }

                AnimatedVisibility(visible = showStatusPicker) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LibraryStatus.entries.forEach { status ->
                            val isSelected = currentStatus == status
                            LiquidGlassChip(
                                label = status.label,
                                isSelected = isSelected,
                                onClick = {
                                    onStatusChange(status)
                                    showStatusPicker = false
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Screenshots Gallery Carousel
        if (game.screenshots.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "GALLERY & SCREENSHOTS (${game.screenshots.size})",
                        color = FitmanColors.TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(game.screenshots) { ss ->
                            Box(
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(124.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, FitmanColors.GlassBorderHighlight, RoundedCornerShape(16.dp))
                                    .clickable { selectedScreenshot = ss }
                            ) {
                                AsyncImage(
                                    model = ss.thumbnail,
                                    contentDescription = "Screenshot",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }

        // Specifications Panel
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "METADATA & DETAILS",
                            color = FitmanColors.TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )

                        DetailRow(
                            icon = Icons.Default.Code,
                            label = "Developer",
                            value = game.developer.ifBlank { "Independent" }
                        )

                        DetailRow(
                            icon = Icons.Default.Public,
                            label = "Publisher",
                            value = game.publisher.ifBlank { "Steam" }
                        )

                        DetailRow(
                            icon = Icons.Default.LocalOffer,
                            label = "Store Price",
                            value = game.price.ifBlank { "Check Steam" },
                            valueColor = FitmanColors.ElectricLime
                        )

                        if (game.releaseDate.isNotBlank()) {
                            DetailRow(
                                icon = Icons.Default.CalendarToday,
                                label = "Release Date",
                                value = game.releaseDate
                            )
                        }

                        if (game.recommendationsCount > 0) {
                            DetailRow(
                                icon = Icons.Default.ThumbUp,
                                label = "Positive Player Ratings",
                                value = "${game.recommendationsCount} players",
                                valueColor = FitmanColors.ElectricLime
                            )
                        }
                    }
                }
            }
        }

        // Genres & Categories
        if (game.genres.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "GENRES & TAGS",
                        color = FitmanColors.TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        game.genres.forEach { genre ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(FitmanColors.GlassSurfaceElevated)
                                    .border(1.dp, FitmanColors.GlassBorderSubtle, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = genre,
                                    color = FitmanColors.TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // About Description
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "ABOUT THIS GAME",
                            color = FitmanColors.TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = game.description.ifBlank { "No official description provided by publisher." },
                            color = Color(0xFFD6DBE5),
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }

        // System Requirements (Expandable)
        if (game.pcRequirementsMin.isNotBlank() || game.pcRequirementsRec.isNotBlank()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    LiquidGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showSpecs = !showSpecs }
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Computer,
                                        contentDescription = null,
                                        tint = FitmanColors.ElectricLime,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "PC HARDWARE REQUIREMENTS",
                                        color = FitmanColors.TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                }

                                Icon(
                                    imageVector = if (showSpecs) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = FitmanColors.TextMuted
                                )
                            }

                            AnimatedVisibility(visible = showSpecs) {
                                Column(
                                    modifier = Modifier.padding(top = 14.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    if (game.pcRequirementsMin.isNotBlank()) {
                                        Text(
                                            text = "MINIMUM SPECIFICATIONS:",
                                            color = FitmanColors.ElectricLime,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = game.pcRequirementsMin,
                                            color = FitmanColors.TextSecondary,
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp
                                        )
                                    }

                                    if (game.pcRequirementsRec.isNotBlank()) {
                                        Text(
                                            text = "RECOMMENDED SPECIFICATIONS:",
                                            color = FitmanColors.ElectricLime,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = game.pcRequirementsRec,
                                            color = FitmanColors.TextSecondary,
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlatformBadge(
    text: String,
    icon: ImageVector? = null
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(FitmanColors.GlassSurfaceElevated)
            .border(1.dp, FitmanColors.GlassBorderSubtle, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = FitmanColors.ElectricLime,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = text,
                color = FitmanColors.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = FitmanColors.TextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FitmanColors.TextMuted,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                color = FitmanColors.TextSecondary,
                fontSize = 13.sp
            )
        }

        Text(
            text = value,
            color = valueColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
