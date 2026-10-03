package com.example.core.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.neuPressed

@Composable
fun BrandIconWell(
    iconKey: String,
    serviceName: String,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
    shapeRadius: Dp = 16.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .neuPressed(
                shape = RoundedCornerShape(shapeRadius),
                cornerRadius = shapeRadius,
                depth = 3.dp,
                backgroundColor = Color(0xFFF0F3F8)
            ),
        contentAlignment = Alignment.Center
    ) {
        val key = iconKey.lowercase()
        when {
            key == "google" || serviceName.contains("Google", ignoreCase = true) -> {
                GoogleLogo(size = size * 0.52f)
            }
            key == "github" || serviceName.contains("GitHub", ignoreCase = true) -> {
                GitHubLogo(size = size * 0.52f)
            }
            key == "spotify" || serviceName.contains("Spotify", ignoreCase = true) -> {
                SpotifyLogo(size = size * 0.52f)
            }
            key == "apple" || serviceName.contains("Apple", ignoreCase = true) -> {
                AppleLogo(size = size * 0.52f)
            }
            key == "figma" || serviceName.contains("Figma", ignoreCase = true) -> {
                FigmaLogo(size = size * 0.48f)
            }
            key == "slack" || serviceName.contains("Slack", ignoreCase = true) -> {
                SlackLogo(size = size * 0.52f)
            }
            key == "netflix" || serviceName.contains("Netflix", ignoreCase = true) -> {
                Text(
                    text = "N",
                    color = Color(0xFFE50914),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
            }
            key == "notion" || serviceName.contains("Notion", ignoreCase = true) -> {
                Text(
                    text = "N",
                    color = OnSurfacePrimary,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            else -> {
                val initial = serviceName.trim().firstOrNull()?.uppercase() ?: "V"
                Text(
                    text = initial,
                    color = ElectricPrimaryBright,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GoogleLogo(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val center = Offset(w / 2f, h / 2f)
        val radius = w * 0.44f

        // Blue right-bar
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )
        // Green bottom
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 45f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )
        // Yellow left
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 135f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )
        // Red top
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 225f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )
        // Inner white knockout
        drawCircle(
            color = Color(0xFFF0F3F8),
            radius = radius * 0.55f,
            center = center
        )
        // Horizontal blue bar
        drawRect(
            color = Color(0xFF4285F4),
            topLeft = Offset(center.x, center.y - (radius * 0.22f)),
            size = Size(radius * 0.95f, radius * 0.44f)
        )
    }
}

@Composable
fun GitHubLogo(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val center = Offset(w / 2f, h / 2f)
        // Outer dark silhouette
        drawCircle(color = Color(0xFF1E293B), radius = w * 0.44f, center = center)
        // Inner cat cutout
        drawCircle(color = Color(0xFFF0F3F8), radius = w * 0.22f, center = Offset(center.x, center.y + (h * 0.12f)))
    }
}

@Composable
fun SpotifyLogo(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val center = Offset(w / 2f, w / 2f)
        drawCircle(color = Color(0xFF1DB954), radius = w * 0.45f, center = center)
        // 3 Sound waves
        val dark = Color(0xFF181C20)
        drawArc(
            color = dark,
            startAngle = 195f,
            sweepAngle = 60f,
            useCenter = false,
            topLeft = Offset(w * 0.22f, w * 0.26f),
            size = Size(w * 0.56f, w * 0.44f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.08f)
        )
        drawArc(
            color = dark,
            startAngle = 195f,
            sweepAngle = 55f,
            useCenter = false,
            topLeft = Offset(w * 0.26f, w * 0.38f),
            size = Size(w * 0.48f, w * 0.36f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.07f)
        )
        drawArc(
            color = dark,
            startAngle = 195f,
            sweepAngle = 50f,
            useCenter = false,
            topLeft = Offset(w * 0.30f, w * 0.50f),
            size = Size(w * 0.40f, w * 0.28f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.06f)
        )
    }
}

@Composable
fun AppleLogo(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val center = Offset(w / 2f, h / 2f)
        drawCircle(color = Color(0xFF1E293B), radius = w * 0.38f, center = Offset(center.x, center.y + h * 0.04f))
        // Bite cutout
        drawCircle(color = Color(0xFFF0F3F8), radius = w * 0.16f, center = Offset(center.x + w * 0.32f, center.y))
        // Leaf
        drawOval(
            color = Color(0xFF1E293B),
            topLeft = Offset(center.x - w * 0.02f, center.y - h * 0.42f),
            size = Size(w * 0.20f, h * 0.14f)
        )
    }
}

@Composable
fun FigmaLogo(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val r = w * 0.24f
        // Top-left Orange
        drawCircle(color = Color(0xFFF24E1E), radius = r, center = Offset(r, r))
        // Top-right Pink
        drawCircle(color = Color(0xFFFF7262), radius = r, center = Offset(w - r, r))
        // Mid-left Purple
        drawCircle(color = Color(0xFFA259FF), radius = r, center = Offset(r, w * 0.5f))
        // Mid-right Blue
        drawCircle(color = Color(0xFF1ABCFE), radius = r, center = Offset(w - r, w * 0.5f))
        // Bottom-left Green
        drawCircle(color = Color(0xFF0ACF83), radius = r, center = Offset(r, w - r))
    }
}

@Composable
fun SlackLogo(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val center = Offset(w / 2f, w / 2f)
        // Red, Blue, Green, Yellow dots
        val r = w * 0.11f
        drawCircle(color = Color(0xFF36C5F0), radius = r, center = Offset(w * 0.28f, w * 0.50f))
        drawCircle(color = Color(0xFF2EB67D), radius = r, center = Offset(w * 0.50f, w * 0.28f))
        drawCircle(color = Color(0xFFECB22E), radius = r, center = Offset(w * 0.72f, w * 0.50f))
        drawCircle(color = Color(0xFFE01E5A), radius = r, center = Offset(w * 0.50f, w * 0.72f))
    }
}
