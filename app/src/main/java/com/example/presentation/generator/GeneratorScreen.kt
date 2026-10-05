package com.example.presentation.generator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.components.TactileSlider
import com.example.core.components.TactileToastPill
import com.example.core.components.TactileToggleSwitch
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SecurityTertiaryBright
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.util.rememberScreenContentPadding
import com.example.core.designsystem.neuFlat
import com.example.core.designsystem.neuPressed
import kotlinx.coroutines.delay

@Composable
fun GeneratorScreen(
    viewModel: GeneratorViewModel,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentPadding = rememberScreenContentPadding()
    var refreshRotation by remember { mutableFloatStateOf(0f) }

    val animatedRotation by animateFloatAsState(
        targetValue = refreshRotation,
        animationSpec = tween(durationMillis = 350),
        label = "refreshAnim"
    )

    // Auto-clear toast
    LaunchedEffect(uiState.toastMessage) {
        if (uiState.toastMessage != null) {
            delay(2500)
            viewModel.handleIntent(GeneratorUiIntent.ClearToast)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // TOP BAR
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Generator",
                            style = VaultTypography.headlineLarge,
                            color = OnSurfacePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Profile Button (Tactile Raised Disc)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("generator_profile_btn")
                            .neuFlat(
                                shape = CircleShape,
                                cornerRadius = 20.dp,
                                backgroundColor = SurfaceContainerLowest
                            )
                            .clickable { onNavigateToSettings() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Security Profile",
                            tint = SecondarySlate,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // PASSWORD PREVIEW DISPLAY (Primary Focal Card)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .neuFlat(
                            shape = RoundedCornerShape(24.dp),
                            cornerRadius = 24.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Monospace Display Recessed Field
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .neuPressed(
                                    shape = RoundedCornerShape(18.dp),
                                    cornerRadius = 18.dp,
                                    backgroundColor = Color(0xFFEFF2F7)
                                )
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = uiState.generatedPassword,
                                    style = VaultTypography.labelLarge,
                                    color = OnSurfacePrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 1.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("generated_password_text")
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Entropy badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF6FFBBE).copy(alpha = 0.35f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${uiState.entropyBits} BITS",
                                        style = VaultTypography.labelSmall,
                                        color = SecurityTertiaryBright,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        // Utility Actions (Refresh + Copy)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .background(ElectricPrimaryBright, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Cryptographic PRNG",
                                    style = VaultTypography.labelSmall,
                                    color = SecondarySlate,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                // Refresh Button
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .testTag("regenerate_btn")
                                        .neuFlat(
                                            shape = CircleShape,
                                            cornerRadius = 19.dp,
                                            backgroundColor = SurfaceContainerLowest
                                        )
                                        .clickable {
                                            refreshRotation += 180f
                                            viewModel.handleIntent(GeneratorUiIntent.Regenerate)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Regenerate",
                                        tint = OnSurfacePrimary,
                                        modifier = Modifier
                                            .size(19.dp)
                                            .rotate(animatedRotation)
                                    )
                                }

                                // Quick Copy Button
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .testTag("quick_copy_btn")
                                        .neuFlat(
                                            shape = CircleShape,
                                            cornerRadius = 19.dp,
                                            backgroundColor = SurfaceContainerLowest
                                        )
                                        .clickable { viewModel.handleIntent(GeneratorUiIntent.CopyPassword) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = if (uiState.isCopied) SecurityTertiaryBright else ElectricPrimaryBright,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // PASSWORD LENGTH CONTROLLER CARD
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .neuFlat(
                            shape = RoundedCornerShape(24.dp),
                            cornerRadius = 24.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Length",
                                    style = VaultTypography.headlineSmall,
                                    color = OnSurfacePrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Recommended: 16+ chars",
                                    style = VaultTypography.bodySmall,
                                    color = SecondarySlate
                                )
                            }

                            // Length Value Pill
                            Box(
                                modifier = Modifier
                                    .neuPressed(
                                        shape = RoundedCornerShape(14.dp),
                                        cornerRadius = 14.dp,
                                        backgroundColor = Color(0xFFEFF2F7)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${uiState.config.length}",
                                    style = VaultTypography.labelMedium,
                                    color = ElectricPrimaryBright,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Tactile Slider
                        TactileSlider(
                            value = uiState.config.length.toFloat(),
                            onValueChange = { raw ->
                                val length = raw.toInt().coerceIn(8, 32)
                                if (length != uiState.config.length) {
                                    viewModel.handleIntent(GeneratorUiIntent.UpdateLength(length))
                                }
                            },
                            valueRange = 8f..32f,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        // Marker labels
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "8", style = VaultTypography.labelSmall, color = SecondarySlate)
                            Text(text = "20", style = VaultTypography.labelSmall, color = SecondarySlate)
                            Text(text = "32", style = VaultTypography.labelSmall, color = SecondarySlate)
                        }
                    }
                }
            }

            // COMPLEXITY & CHARACTER ATTRIBUTES
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .neuFlat(
                            shape = RoundedCornerShape(24.dp),
                            cornerRadius = 24.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Attributes",
                                style = VaultTypography.headlineSmall,
                                color = OnSurfacePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Entropy Modifiers",
                                style = VaultTypography.labelSmall,
                                color = SecondarySlate
                            )
                        }

                        // Row 1: Uppercase
                        AttributeRow(
                            glyph = "A",
                            title = "Uppercase Letters",
                            subtitle = "A-Z",
                            checked = uiState.config.includeUppercase,
                            onCheckedChange = { viewModel.handleIntent(GeneratorUiIntent.ToggleUppercase(it)) }
                        )

                        // Row 2: Lowercase
                        AttributeRow(
                            glyph = "a",
                            title = "Lowercase Letters",
                            subtitle = "a-z",
                            checked = uiState.config.includeLowercase,
                            onCheckedChange = { viewModel.handleIntent(GeneratorUiIntent.ToggleLowercase(it)) }
                        )

                        // Row 3: Numbers
                        AttributeRow(
                            glyph = "0",
                            title = "Numbers",
                            subtitle = "0-9",
                            checked = uiState.config.includeNumbers,
                            onCheckedChange = { viewModel.handleIntent(GeneratorUiIntent.ToggleNumbers(it)) }
                        )

                        // Row 4: Symbols
                        AttributeRow(
                            glyph = "#",
                            title = "Symbols",
                            subtitle = "!@#$%^&*()_+-=",
                            checked = uiState.config.includeSymbols,
                            onCheckedChange = { viewModel.handleIntent(GeneratorUiIntent.ToggleSymbols(it)) }
                        )
                    }
                }
            }
        }

        // Floating Toast Pill
        TactileToastPill(
            message = uiState.toastMessage ?: "",
            visible = uiState.toastMessage != null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp)
        )
    }
}

@Composable
private fun AttributeRow(
    glyph: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEFF2F7)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = glyph,
                    style = VaultTypography.labelMedium,
                    color = OnSurfacePrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = VaultTypography.bodyMedium,
                    color = OnSurfacePrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = VaultTypography.labelSmall,
                    color = SecondarySlate,
                    fontSize = 10.sp
                )
            }
        }

        TactileToggleSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
