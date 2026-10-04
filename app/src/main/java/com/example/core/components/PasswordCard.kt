package com.example.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.crypto.TotpManager
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.OnSurfaceSecondary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SecurityTertiaryBright
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.designsystem.neuPressed
import com.example.domain.model.Credential
import kotlinx.coroutines.delay

@Composable
fun PasswordCard(
    credential: Credential,
    onCopyPassword: (String) -> Unit,
    onCopyUsername: (String) -> Unit,
    onEdit: (Credential) -> Unit,
    onDelete: (Long) -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRevealed by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    var totpRemaining by remember { mutableStateOf(TotpManager.getRemainingSeconds()) }
    var currentTotp by remember { mutableStateOf("") }

    // Auto-hide password after 8 seconds for security
    LaunchedEffect(isRevealed) {
        if (isRevealed) {
            delay(8000)
            isRevealed = false
        }
    }

    // Refresh TOTP timer if present
    LaunchedEffect(credential.totpSecret, isExpanded) {
        if (credential.totpSecret.isNotBlank() && isExpanded) {
            while (true) {
                currentTotp = TotpManager.getCurrentTotpCode(credential.totpSecret)
                totpRemaining = TotpManager.getRemainingSeconds()
                delay(1000)
            }
        }
    }

    val cardShape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("credential_card_${credential.service.lowercase()}")
            .neuFlat(
                shape = cardShape,
                cornerRadius = 24.dp,
                backgroundColor = SurfaceContainerLowest
            )
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Brand Icon + Title + Username + Mask
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    BrandIconWell(
                        iconKey = credential.iconKey,
                        serviceName = credential.service,
                        size = 46.dp,
                        shapeRadius = 16.dp
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = credential.service,
                                style = VaultTypography.headlineSmall,
                                color = OnSurfacePrimary,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (credential.isFavorite) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Favorite",
                                    tint = Color(0xFFF59E0B),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Text(
                            text = credential.username,
                            style = VaultTypography.bodySmall,
                            color = OnSurfaceSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .testTag("username_${credential.id}")
                                .combinedClickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { isExpanded = !isExpanded },
                                    onLongClick = {
                                        if (credential.username.isNotBlank()) {
                                            onCopyUsername(credential.username)
                                        }
                                    }
                                )
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = if (isRevealed) credential.password else "••••••••",
                            style = VaultTypography.labelSmall,
                            color = if (isRevealed) ElectricPrimaryBright else SecondarySlate,
                            letterSpacing = if (isRevealed) 0.5.sp else 2.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Quick copy password + reveal — username: long-press the email/username text
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("quick_copy_password_${credential.id}")
                            .neuFlat(
                                shape = CircleShape,
                                cornerRadius = 19.dp,
                                backgroundColor = SurfaceContainerLowest
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onCopyPassword(credential.password) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy password",
                            tint = ElectricPrimaryBright,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("eye_toggle_${credential.id}")
                            .then(
                                if (isRevealed) {
                                    Modifier.neuPressed(
                                        shape = CircleShape,
                                        cornerRadius = 19.dp,
                                        backgroundColor = Color(0xFFE5EDFC)
                                    )
                                } else {
                                    Modifier.neuFlat(
                                        shape = CircleShape,
                                        cornerRadius = 19.dp,
                                        backgroundColor = SurfaceContainerLowest
                                    )
                                }
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { isRevealed = !isRevealed },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isRevealed) "Hide Password" else "Show Password",
                            tint = if (isRevealed) ElectricPrimaryBright else SecondarySlate,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Expanded Card Action Panel
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE2E8F0))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (credential.totpSecret.isNotBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .neuPressed(
                                    shape = RoundedCornerShape(14.dp),
                                    cornerRadius = 14.dp,
                                    backgroundColor = Color(0xFFF1F5F9)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    tint = SecurityTertiaryBright,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "2FA TOTP: ",
                                    style = VaultTypography.bodySmall,
                                    color = SecondarySlate
                                )
                                Text(
                                    text = currentTotp.ifBlank { "••••••" },
                                    style = VaultTypography.labelLarge,
                                    color = SecurityTertiaryBright,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            }
                            Text(
                                text = "${totpRemaining}s",
                                style = VaultTypography.labelSmall,
                                color = SecondarySlate
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .neuFlat(
                                    shape = RoundedCornerShape(12.dp),
                                    cornerRadius = 12.dp,
                                    backgroundColor = SurfaceCanvas
                                )
                                .clickable { onCopyPassword(credential.password) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = ElectricPrimaryBright,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Password",
                                    style = VaultTypography.bodySmall,
                                    color = ElectricPrimaryBright,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .neuFlat(
                                    shape = RoundedCornerShape(12.dp),
                                    cornerRadius = 12.dp,
                                    backgroundColor = SurfaceCanvas
                                )
                                .clickable { onCopyUsername(credential.username) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Username",
                                style = VaultTypography.bodySmall,
                                color = SecondarySlate,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .neuFlat(
                                    shape = CircleShape,
                                    cornerRadius = 16.dp,
                                    backgroundColor = SurfaceCanvas
                                )
                                .clickable { onEdit(credential) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = SecondarySlate,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .neuFlat(
                                    shape = CircleShape,
                                    cornerRadius = 16.dp,
                                    backgroundColor = SurfaceCanvas
                                )
                                .clickable { onToggleFavorite(credential.id, !credential.isFavorite) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (credential.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Favorite",
                                tint = if (credential.isFavorite) Color(0xFFF59E0B) else SecondarySlate,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .neuFlat(
                                    shape = CircleShape,
                                    cornerRadius = 16.dp,
                                    backgroundColor = SurfaceCanvas
                                )
                                .clickable { onDelete(credential.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFBA1A1A),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
