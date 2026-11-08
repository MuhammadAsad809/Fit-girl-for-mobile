package com.fitman.nativeapp.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitman.nativeapp.ui.components.GameHubTopBar
import com.fitman.nativeapp.ui.theme.FitmanColors

data class CommunityPost(
    val id: String,
    val author: String,
    val timeAgo: String,
    val gameTag: String,
    val title: String,
    val content: String,
    val upvotes: Int,
    val comments: Int
)

/**
 * GameHub Community Feed Screen with discussions, reviews, and setup guides.
 */
@Composable
fun CommunityScreen(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val posts = listOf(
        CommunityPost(
            id = "1",
            author = "ValkyriePilot",
            timeAgo = "2h ago",
            gameTag = "#GTA V Legacy",
            title = "Ultra-Realistic 4K Graphic Mod Settings on Handhelds",
            content = "For those running GTA V on Steam Deck and Android remote play, enable DX11 medium shadows with GSFG 1.1 frame interpolation for locked 60 FPS.",
            upvotes = 342,
            comments = 58
        ),
        CommunityPost(
            id = "2",
            author = "RepackMaster",
            timeAgo = "4h ago",
            gameTag = "#FitGirl Repacks",
            title = "Direct Torrent Downloader Stability Report",
            content = "All full game repack torrents and magnets now verify SHA-256 chunks prior to extraction. Download times are down by 35% on mobile clients.",
            upvotes = 512,
            comments = 89
        ),
        CommunityPost(
            id = "3",
            author = "CyberSamurai",
            timeAgo = "6h ago",
            gameTag = "#Witcher 3 Wild Hunt",
            title = "Next-Gen Ray Tracing Performance Guide",
            content = "Tested the latest repack patch with HairWorks disabled. Massive performance uplift on midrange GPUs with zero visual degradation.",
            upvotes = 220,
            comments = 41
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FitmanColors.WarmBackgroundGradient)
    ) {
        GameHubTopBar(
            queryText = "Search community discussions...",
            onSearchClick = onSearchClick
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Player Community",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(posts) { post ->
                CommunityPostCard(post = post)
            }
        }
    }
}

@Composable
private fun CommunityPostCard(post: CommunityPost) {
    val cardShape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(FitmanColors.WarmCardBackground)
            .border(1.dp, FitmanColors.WarmCardBorder, cardShape)
            .padding(16.dp)
    ) {
        Column {
            // Author row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF382E25)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.author.take(1).uppercase(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.author,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = post.timeAgo,
                        color = FitmanColors.TextMutedWarm,
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x33C5A059))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = post.gameTag,
                        color = FitmanColors.StarGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Post Title
            Text(
                text = post.title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            // Post Content
            Text(
                text = post.content,
                color = Color(0xFFD6D0C9),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(14.dp))

            // Stats row
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Likes",
                        tint = FitmanColors.TextMutedWarm,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "${post.upvotes}",
                        color = FitmanColors.TextMutedWarm,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = FitmanColors.TextMutedWarm,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "${post.comments}",
                        color = FitmanColors.TextMutedWarm,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
