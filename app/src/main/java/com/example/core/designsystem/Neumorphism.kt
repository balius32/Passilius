package com.example.core.designsystem

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Creates a dual-light Neumorphic extruded shadow effect (flat/elevated).
 */
fun Modifier.neuFlat(
    shape: Shape = RoundedCornerShape(24.dp),
    cornerRadius: Dp = 24.dp,
    elevation: Dp = 6.dp,
    lightColor: Color = Color(0xFFFFFFFF),
    darkColor: Color = Color(0x70CAD3DF),
    backgroundColor: Color = SurfaceCanvas
): Modifier = this
    .drawBehind {
        val radiusPx = cornerRadius.toPx()
        val elevationPx = elevation.toPx()

        drawIntoCanvas { canvas ->
            // Light Specular Highlight (Top-Left)
            val lightPaint = Paint().apply {
                asFrameworkPaint().apply {
                    color = lightColor.toArgb()
                    isAntiAlias = true
                    maskFilter = BlurMaskFilter(elevationPx * 1.5f, BlurMaskFilter.Blur.NORMAL)
                }
            }
            canvas.drawRoundRect(
                left = -elevationPx * 0.8f,
                top = -elevationPx * 0.8f,
                right = size.width - elevationPx * 0.2f,
                bottom = size.height - elevationPx * 0.2f,
                radiusX = radiusPx,
                radiusY = radiusPx,
                paint = lightPaint
            )

            // Dark Ambient Shadow (Bottom-Right)
            val darkPaint = Paint().apply {
                asFrameworkPaint().apply {
                    color = darkColor.toArgb()
                    isAntiAlias = true
                    maskFilter = BlurMaskFilter(elevationPx * 1.8f, BlurMaskFilter.Blur.NORMAL)
                }
            }
            canvas.drawRoundRect(
                left = elevationPx * 0.6f,
                top = elevationPx * 0.6f,
                right = size.width + elevationPx * 0.8f,
                bottom = size.height + elevationPx * 0.8f,
                radiusX = radiusPx,
                radiusY = radiusPx,
                paint = darkPaint
            )
        }
    }
    .background(backgroundColor, shape)
    .clip(shape)

/**
 * Creates a debossed/pressed recessed well effect.
 */
fun Modifier.neuPressed(
    shape: Shape = RoundedCornerShape(18.dp),
    cornerRadius: Dp = 18.dp,
    depth: Dp = 4.dp,
    insetDarkColor: Color = Color(0x50BAC7D5),
    insetLightColor: Color = Color(0xAAFFFFFF),
    backgroundColor: Color = Color(0xFFEFF2F7)
): Modifier = this
    .background(backgroundColor, shape)
    .drawBehind {
        val radiusPx = cornerRadius.toPx()
        val depthPx = depth.toPx()

        drawIntoCanvas { canvas ->
            // Inner dark shadow on top-left edge
            val darkPaint = Paint().apply {
                style = PaintingStyle.Stroke
                strokeWidth = depthPx * 1.5f
                asFrameworkPaint().apply {
                    color = insetDarkColor.toArgb()
                    isAntiAlias = true
                    maskFilter = BlurMaskFilter(depthPx * 1.2f, BlurMaskFilter.Blur.NORMAL)
                }
            }
            canvas.drawRoundRect(
                left = depthPx * 0.3f,
                top = depthPx * 0.3f,
                right = size.width - depthPx * 0.3f,
                bottom = size.height - depthPx * 0.3f,
                radiusX = radiusPx,
                radiusY = radiusPx,
                paint = darkPaint
            )

            // Subtle bottom-right highlight reflection
            val lightPaint = Paint().apply {
                style = PaintingStyle.Stroke
                strokeWidth = depthPx
                asFrameworkPaint().apply {
                    color = insetLightColor.toArgb()
                    isAntiAlias = true
                    maskFilter = BlurMaskFilter(depthPx, BlurMaskFilter.Blur.NORMAL)
                }
            }
            canvas.drawRoundRect(
                left = depthPx,
                top = depthPx,
                right = size.width,
                bottom = size.height,
                radiusX = radiusPx,
                radiusY = radiusPx,
                paint = lightPaint
            )
        }
    }
    .clip(shape)

/**
 * Tactile Button modifier that animates smoothly between flat and pressed state upon interaction.
 */
@Composable
fun Modifier.neuClickable(
    cornerRadius: Dp = 22.dp,
    shape: Shape = RoundedCornerShape(22.dp),
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedElevation by animateFloatAsState(
        targetValue = if (isPressed) 1f else 6f,
        animationSpec = tween(durationMillis = 150),
        label = "neuElevation"
    )

    return if (isPressed) {
        this
            .neuPressed(
                shape = shape,
                cornerRadius = cornerRadius,
                depth = 4.dp
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    } else {
        this
            .neuFlat(
                shape = shape,
                cornerRadius = cornerRadius,
                elevation = animatedElevation.dp
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    }
}
