package com.fitman.nativeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fitman.nativeapp.ui.theme.FitmanColors

@Composable
fun LiquidGlassDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    confirmButtonText: String = "OK",
    onConfirm: () -> Unit = onDismissRequest,
    dismissButtonText: String? = null,
    onDismiss: (() -> Unit)? = null
) {
    Dialog(onDismissRequest = onDismissRequest) {
        val shape = RoundedCornerShape(26.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 24.dp, shape = shape, spotColor = Color.Black)
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xF0151C2C),
                            Color(0xF50A0E18)
                        )
                    )
                )
                .border(width = 1.dp, brush = FitmanColors.SpecularBorderBrush, shape = shape)
                .padding(22.dp)
        ) {
            // Specular top rim
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(0.9f)
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, Color(0x66FFFFFF), Color.Transparent)
                        )
                    )
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = FitmanColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = message,
                    color = FitmanColors.TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (dismissButtonText != null && onDismiss != null) {
                        LiquidGlassButton(
                            text = dismissButtonText,
                            onClick = onDismiss,
                            isPrimary = false,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                    LiquidGlassButton(
                        text = confirmButtonText,
                        onClick = onConfirm,
                        isPrimary = true
                    )
                }
            }
        }
    }
}
