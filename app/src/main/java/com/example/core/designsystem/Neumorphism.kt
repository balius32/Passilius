package com.example.core.designsystem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Flat raised surface with a subtle border (no soft glow / blur shadows).
 * [cornerRadius] is retained for call-site compatibility with the previous API.
 */
@Suppress("UNUSED_PARAMETER")
fun Modifier.neuFlat(
    shape: Shape = RoundedCornerShape(24.dp),
    cornerRadius: Dp = 24.dp,
    backgroundColor: Color = SurfaceCanvas,
    strokeColor: Color = SoftStroke,
    borderWidth: Dp = 1.dp
): Modifier = this
    .background(backgroundColor, shape)
    .border(borderWidth, strokeColor, shape)
    .clip(shape)

/**
 * Flat recessed surface with a subtle border (no inset blur shadows).
 * [cornerRadius] is retained for call-site compatibility with the previous API.
 */
@Suppress("UNUSED_PARAMETER")
fun Modifier.neuPressed(
    shape: Shape = RoundedCornerShape(18.dp),
    cornerRadius: Dp = 18.dp,
    backgroundColor: Color = Color(0xFFEFF2F7),
    strokeColor: Color = SoftStroke,
    borderWidth: Dp = 1.dp
): Modifier = this
    .background(backgroundColor, shape)
    .border(borderWidth, strokeColor, shape)
    .clip(shape)

/**
 * Tactile button modifier that scales slightly on press without glow elevation.
 */
@Composable
fun Modifier.neuClickable(
    cornerRadius: Dp = 22.dp,
    shape: Shape = RoundedCornerShape(22.dp),
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "neuPressScale"
    )

    return this
        .scale(pressScale)
        .then(
            if (isPressed) {
                Modifier.neuPressed(
                    shape = shape,
                    cornerRadius = cornerRadius
                )
            } else {
                Modifier.neuFlat(
                    shape = shape,
                    cornerRadius = cornerRadius
                )
            }
        )
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
}
