package com.example.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhonelinkLock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.TactileToggleSwitch
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnPrimary
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SecurityTertiaryBright
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.util.rememberScreenContentPadding
import com.example.core.designsystem.neuFlat
import com.example.core.designsystem.neuPressed

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLockVault: () -> Unit,
    modifier: Modifier = Modifier
) {
    var biometricEnabled by remember { mutableStateOf(true) }
    var autoClearClipboard by remember { mutableStateOf(true) }
    var zeroKnowledgeSync by remember { mutableStateOf(false) }
    val contentPadding = rememberScreenContentPadding(bottom = 60.dp)

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
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .neuFlat(
                                shape = CircleShape,
                                cornerRadius = 20.dp,
                                backgroundColor = SurfaceContainerLowest
                            )
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurfacePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Security Profile",
                            style = VaultTypography.headlineLarge,
                            color = OnSurfacePrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Swiss Zero-Knowledge Architecture",
                            style = VaultTypography.labelSmall,
                            color = SecondarySlate
                        )
                    }
                }
            }

            // Security Health Score Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .neuFlat(
                            shape = RoundedCornerShape(24.dp),
                            cornerRadius = 24.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .neuPressed(
                                            shape = CircleShape,
                                            cornerRadius = 22.dp,
                                            backgroundColor = Color(0xFFE5F8EE)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = SecurityTertiaryBright,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Vault Health",
                                        style = VaultTypography.headlineSmall,
                                        color = OnSurfacePrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Grade: Military-Grade",
                                        style = VaultTypography.labelSmall,
                                        color = SecurityTertiaryBright,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Text(
                                text = "96%",
                                style = VaultTypography.displayLarge,
                                color = SecurityTertiaryBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                        }

                        // Metrics Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MetricItem("Accounts", "24")
                            MetricItem("Strong", "21")
                            MetricItem("Reused", "0")
                            MetricItem("2FA Active", "14")
                        }
                    }
                }
            }

            // Security Protocols Section
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
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "Hardware Encryption & Access",
                            style = VaultTypography.headlineSmall,
                            color = OnSurfacePrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        // Row 1: Biometrics
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    tint = ElectricPrimaryBright,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Biometric Unlock",
                                        style = VaultTypography.bodyMedium,
                                        color = OnSurfacePrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Touch ID / Face Unlock authentication",
                                        style = VaultTypography.bodySmall,
                                        color = SecondarySlate,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            TactileToggleSwitch(
                                checked = biometricEnabled,
                                onCheckedChange = { biometricEnabled = it }
                            )
                        }

                        // Row 2: Clipboard Clear
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = ElectricPrimaryBright,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Auto-Clear Clipboard",
                                        style = VaultTypography.bodyMedium,
                                        color = OnSurfacePrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Clears secrets after 30 seconds",
                                        style = VaultTypography.bodySmall,
                                        color = SecondarySlate,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            TactileToggleSwitch(
                                checked = autoClearClipboard,
                                onCheckedChange = { autoClearClipboard = it }
                            )
                        }

                        // Row 3: KeyStore Spec
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = SecurityTertiaryBright,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Cipher Architecture",
                                        style = VaultTypography.bodyMedium,
                                        color = OnSurfacePrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "AES-256-GCM Hardware-Backed KeyStore",
                                        style = VaultTypography.bodySmall,
                                        color = SecondarySlate,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Lock Vault Now CTA
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("lock_vault_now_btn")
                        .neuFlat(
                            shape = RoundedCornerShape(22.dp),
                            cornerRadius = 22.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .clickable { onLockVault() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFFBA1A1A),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Lock Vault Now",
                            style = VaultTypography.headlineSmall,
                            color = Color(0xFFBA1A1A),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = VaultTypography.headlineMedium,
            color = OnSurfacePrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = VaultTypography.labelSmall,
            color = SecondarySlate,
            fontSize = 11.sp
        )
    }
}
