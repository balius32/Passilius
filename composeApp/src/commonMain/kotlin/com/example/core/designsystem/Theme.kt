package com.example.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = ElectricPrimaryBright,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = SurfaceContainerLowest,
    secondary = SecondarySlate,
    onSecondary = OnPrimary,
    secondaryContainer = Color(0xFFD5E0F8),
    onSecondaryContainer = Color(0xFF586377),
    tertiary = SecurityTertiary,
    onTertiary = OnPrimary,
    tertiaryContainer = SecurityTertiaryBright,
    onTertiaryContainer = Color(0xFFF5FFF6),
    background = SurfaceCanvas,
    onBackground = OnSurfacePrimary,
    surface = SurfaceCanvas,
    onSurface = OnSurfacePrimary,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = OnSurfaceSecondary,
    outline = OutlineColor,
    outlineVariant = OutlineVariant,
    error = SecurityDanger,
    onError = OnPrimary,
    errorContainer = ErrorContainer
)

val VaultShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(26.dp)
)

@Composable
fun VaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = VaultTypography,
        shapes = VaultShapes,
        content = content
    )
}
