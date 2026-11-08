package com.fitman.nativeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fitman.nativeapp.ui.theme.FitmanColors

@Composable
fun LiquidGlassSheetContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 24.dp, shape = sheetShape, spotColor = Color.Black)
            .clip(sheetShape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xF2131926),
                        Color(0xFA070A10)
                    )
                )
            )
            .border(width = 1.dp, brush = FitmanColors.SpecularBorderBrush, shape = sheetShape)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pill pull bar
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0x40FFFFFF))
            )

            Spacer(Modifier.height(16.dp))

            content()
        }
    }
}
