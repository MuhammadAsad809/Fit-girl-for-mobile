package com.fitman.nativeapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitman.nativeapp.ui.theme.FitmanColors

data class SegmentItem(
    val key: String,
    val label: String,
    val icon: ImageVector? = null
)

/**
 * Apple iOS 45-inspired Liquid Glass Segmented Capsule Bar.
 * Unified translucent floating obsidian capsule with glowing specular pill thumb.
 */
@Composable
fun LiquidGlassSegmentedBar(
    items: List<SegmentItem>,
    selectedKey: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val capsuleShape = RoundedCornerShape(26.dp)
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(
                elevation = 10.dp,
                shape = capsuleShape,
                ambientColor = Color.Black.copy(alpha = 0.5f),
                spotColor = Color.Black.copy(alpha = 0.7f)
            )
            .clip(capsuleShape)
            .background(FitmanColors.GlassCapsuleGradient)
            .border(
                width = 1.dp,
                brush = FitmanColors.SpecularBorderBrush,
                shape = capsuleShape
            )
            .padding(4.dp)
    ) {
        // Specular highlight line along top edge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            FitmanColors.GlassBorderHighlight.copy(alpha = 0.4f),
                            FitmanColors.SpecularGlint.copy(alpha = 0.6f),
                            FitmanColors.GlassBorderHighlight.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item.key.equals(selectedKey, ignoreCase = true) ||
                        (item.key == "top_sellers" && selectedKey.equals("Discover", ignoreCase = true))

                LiquidSegmentPill(
                    item = item,
                    isSelected = isSelected,
                    onClick = { onItemSelected(item.key) }
                )
            }
        }
    }
}

@Composable
private fun LiquidSegmentPill(
    item: SegmentItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val pillShape = RoundedCornerShape(22.dp)

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.98f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 450f),
        label = "segmentScale"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF070B02) else FitmanColors.TextSecondary,
        label = "segmentTextColor"
    )

    val backgroundModifier = if (isSelected) {
        Modifier
            .shadow(
                elevation = 6.dp,
                shape = pillShape,
                spotColor = FitmanColors.ElectricLimeGlow
            )
            .background(brush = FitmanColors.LimeSpecularBrush, shape = pillShape)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(Color(0x99FFFFFF), Color(0x33FFFFFF))
                ),
                shape = pillShape
            )
    } else {
        Modifier
            .background(Color.Transparent, shape = pillShape)
    }

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(pillShape)
            .then(backgroundModifier)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (item.icon != null) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = item.label,
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
