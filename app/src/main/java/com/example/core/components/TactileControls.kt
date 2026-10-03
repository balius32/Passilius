package com.example.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnPrimary
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.designsystem.neuPressed

/**
 * Tactile Neumorphic Button with distinct extrusion and physical tap deflection.
 */
@Composable
fun NeumorphicButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPrimary: Boolean = false,
    enabled: Boolean = true,
    height: Dp = 56.dp,
    shapeRadius: Dp = 22.dp,
    testTag: String = "neu_button"
) {
    val shape = RoundedCornerShape(shapeRadius)
    
    val bgModifier = if (isPrimary) {
        Modifier
            .neuFlat(
                shape = shape,
                cornerRadius = shapeRadius,
                elevation = 8.dp,
                backgroundColor = ElectricPrimaryBright,
                darkColor = Color(0x600050D6),
                lightColor = Color(0x8070A4FF)
            )
    } else {
        Modifier
            .neuFlat(
                shape = shape,
                cornerRadius = shapeRadius,
                elevation = 6.dp,
                backgroundColor = SurfaceCanvas
            )
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .height(height)
            .then(bgModifier)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isPrimary) OnPrimary else ElectricPrimaryBright,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = VaultTypography.headlineSmall,
                color = if (isPrimary) OnPrimary else OnSurfacePrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Tactile Neumorphic Switch (horizontal pill debossed track with stone thumb).
 */
@Composable
fun TactileToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "toggle_switch"
) {
    val width = 50.dp
    val height = 28.dp
    val thumbSize = 22.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 3.dp,
        animationSpec = tween(180),
        label = "thumbOffset"
    )

    val trackColor = if (checked) ElectricPrimaryBright else Color(0xFFDFE3E8)

    Box(
        modifier = modifier
            .testTag(testTag)
            .size(width, height)
            .clip(RoundedCornerShape(14.dp))
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .neuFlat(
                    shape = CircleShape,
                    cornerRadius = thumbSize / 2,
                    elevation = 2.dp,
                    backgroundColor = Color.White
                )
        )
    }
}

/**
 * Tactile Neumorphic Slider Track with electric blue fill.
 */
@Composable
fun TactileSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    steps: Int = 0
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val maxPx = constraints.maxWidth.toFloat()
        val rangeSpan = valueRange.endInclusive - valueRange.start
        val fraction = ((value - valueRange.start) / rangeSpan).coerceIn(0f, 1f)

        // Debossed recessed base channel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .neuPressed(
                    shape = RoundedCornerShape(5.dp),
                    cornerRadius = 5.dp,
                    depth = 2.dp,
                    backgroundColor = Color(0xFFE2E8F0)
                )
        ) {
            // Electric fill
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .clip(RoundedCornerShape(5.dp))
                    .background(ElectricPrimaryBright)
            )
        }

        // Thumb disc
        val thumbOffset = ((maxWidth - 26.dp) * fraction).coerceAtLeast(0.dp)
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(26.dp)
                .neuFlat(
                    shape = CircleShape,
                    cornerRadius = 13.dp,
                    elevation = 4.dp,
                    backgroundColor = Color.White
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, _ ->
                        change.consume()
                        val newFraction = (change.position.x / maxPx).coerceIn(0f, 1f)
                        val newValue = valueRange.start + newFraction * rangeSpan
                        onValueChange(newValue)
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val newFraction = (offset.x / maxPx).coerceIn(0f, 1f)
                        val newValue = valueRange.start + newFraction * rangeSpan
                        onValueChange(newValue)
                    }
                }
        )
    }
}

/**
 * Delightful Floating Toast Pill.
 */
@Composable
fun TactileToastPill(
    message: String,
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)) + slideInVertically(initialOffsetY = { -40 }),
        exit = fadeOut(tween(200)) + slideOutVertically(targetOffsetY = { -40 }),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .neuFlat(
                    shape = RoundedCornerShape(20.dp),
                    cornerRadius = 20.dp,
                    elevation = 6.dp,
                    backgroundColor = SurfaceCanvas
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = ElectricPrimaryBright,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = message,
                    style = VaultTypography.bodySmall,
                    color = OnSurfacePrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
