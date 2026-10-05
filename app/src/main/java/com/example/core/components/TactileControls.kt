package com.example.core.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnPrimary
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SurfaceContainerLowest
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
                backgroundColor = ElectricPrimaryBright
            )
    } else {
        Modifier
            .neuFlat(
                shape = shape,
                cornerRadius = shapeRadius,
                backgroundColor = SurfaceContainerLowest
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
                    backgroundColor = Color.White
                )
        )
    }
}

/**
 * Tactile Neumorphic Slider Track with electric blue fill.
 * Gestures are handled on the full track (not the thumb) so the thumb follows the finger.
 */
@Composable
fun TactileSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    steps: Int = 0
) {
    var isDragging by remember { mutableStateOf(false) }
    var localValue by remember { mutableFloatStateOf(value) }
    val onValueChangeState by rememberUpdatedState(onValueChange)

    LaunchedEffect(value) {
        if (!isDragging) {
            localValue = value
        }
    }

    val displayValue = if (isDragging) localValue else value

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val trackWidth = maxWidth
        val maxPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)
        val rangeSpan = (valueRange.endInclusive - valueRange.start).coerceAtLeast(0.0001f)
        val fraction = ((displayValue - valueRange.start) / rangeSpan).coerceIn(0f, 1f)

        fun valueFromX(x: Float): Float {
            val rawFraction = (x / maxPx).coerceIn(0f, 1f)
            var newValue = valueRange.start + rawFraction * rangeSpan
            if (steps > 0) {
                val stepSize = rangeSpan / (steps + 1)
                val stepped =
                    ((newValue - valueRange.start) / stepSize).roundToInt() * stepSize + valueRange.start
                newValue = stepped.coerceIn(valueRange.start, valueRange.endInclusive)
            }
            return newValue
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(valueRange, maxPx, steps) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        isDragging = true
                        val pressed = valueFromX(down.position.x)
                        localValue = pressed
                        onValueChangeState(pressed)

                        drag(down.id) { change ->
                            change.consume()
                            val dragged = valueFromX(change.position.x)
                            localValue = dragged
                            onValueChangeState(dragged)
                        }
                        isDragging = false
                    }
                }
        ) {
            // Debossed recessed base channel
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
                    .height(10.dp)
                    .neuPressed(
                        shape = RoundedCornerShape(5.dp),
                        cornerRadius = 5.dp,
                        backgroundColor = Color(0xFFE2E8F0)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction)
                        .clip(RoundedCornerShape(5.dp))
                        .background(ElectricPrimaryBright)
                )
            }

            // Thumb disc
            val thumbOffset = ((trackWidth - 26.dp) * fraction).coerceAtLeast(0.dp)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = thumbOffset)
                    .size(26.dp)
                    .neuFlat(
                        shape = CircleShape,
                        cornerRadius = 13.dp,
                        backgroundColor = Color.White
                    )
            )
        }
    }
}
