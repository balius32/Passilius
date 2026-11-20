package com.example.presentation.settings

import com.example.core.localization.*

import com.example.core.localization.tr
import passilius.composeapp.generated.resources.*

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.biometric.BiometricSettings
import com.example.core.crypto.BiometricVaultKey
import com.example.core.biometric.BiometricUnlock
import com.example.core.biometric.BiometricUnlockResult
import com.example.core.components.TactileToggleSwitch
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnPrimary
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SecurityTertiaryBright
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.platform.LocalPlatformCapabilities
import com.example.core.util.ClipboardSettings
import com.example.core.util.rememberScreenContentPadding
import com.example.core.designsystem.neuFlat
import com.example.core.designsystem.neuPressed

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onLockVault: () -> Unit,
    onManageCategories: () -> Unit,
    onSyncDevices: () -> Unit = {},
    onChangeMasterPassword: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val securityReport by viewModel.securityReport.collectAsStateWithLifecycle()
    var showLanguageDialog by remember { mutableStateOf(false) }
    if (showLanguageDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(tr(Res.string.language)) },
            text = {
                Column {
                    com.example.core.localization.AppLanguage.entries.forEach { language ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                com.example.core.localization.LanguageController.select(language)
                                showLanguageDialog = false
                            }.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.RadioButton(
                                selected = com.example.core.localization.LanguageController.selection == language,
                                onClick = null
                            )
                            Text(languageLabel(language))
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showLanguageDialog = false }) { Text(tr(Res.string.cancel)) }
            }
        )
    }
    val supportsBiometrics = LocalPlatformCapabilities.current.supportsBiometricUnlock
    var biometricEnabled by remember { mutableStateOf(BiometricSettings.isUnlockEnabled()) }
    var biometricMessage by remember { mutableStateOf<UiMessage?>(null) }
    var autoClearClipboard by remember { mutableStateOf(ClipboardSettings.isAutoClearEnabled()) }
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
                            contentDescription = tr(Res.string.back),
                            tint = OnSurfacePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = tr(Res.string.profile),
                        style = VaultTypography.headlineLarge,
                        color = OnSurfacePrimary,
                        fontWeight = FontWeight.Bold
                    )
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
                                        text = tr(Res.string.vault_health),
                                        style = VaultTypography.headlineSmall,
                                        color = OnSurfacePrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = tr(Res.string.grade, gradeText(securityReport.gradeLabel)),
                                        style = VaultTypography.labelSmall,
                                        color = SecurityTertiaryBright,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Text(
                                text = displayNumber(securityReport.securityScorePercentage) + "%",
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
                            MetricItem(tr(Res.string.accounts), displayNumber(securityReport.totalCount))
                            MetricItem(tr(Res.string.strong), displayNumber(securityReport.strongCount))
                            MetricItem(tr(Res.string.reused), displayNumber(securityReport.reusedCount))
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
                    Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
                        Text(
                            text = tr(Res.string.security),
                            style = VaultTypography.headlineSmall,
                            color = OnSurfacePrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (supportsBiometrics) {
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
                                            text = tr(Res.string.biometric_unlock),
                                            style = VaultTypography.bodyMedium,
                                            color = OnSurfacePrimary,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = tr(Res.string.touch_id_face_unlock_authentication),
                                            style = VaultTypography.bodySmall,
                                            color = SecondarySlate,
                                            fontSize = 11.sp
                                        )
                                        if (!(biometricMessage == null)) {
                                            Text(
                                                text = biometricMessage?.resolve().orEmpty(),
                                                style = VaultTypography.bodySmall,
                                                color = Color(0xFFBA1A1A),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                                TactileToggleSwitch(
                                    checked = biometricEnabled,
                                    onCheckedChange = { enabled ->
                                        if (enabled) {
                                            BiometricUnlock.authenticate(
                                                title = tr(Res.string.enable_biometric_unlock),
                                                subtitle = tr(Res.string.confirm_your_fingerprint_or_face)
                                            ) { result ->
                                                when (result) {
                                                    BiometricUnlockResult.Success -> {
                                                        try {
                                                            BiometricVaultKey.enroll()
                                                            biometricEnabled = true
                                                            biometricMessage = null
                                                            BiometricSettings.setUnlockEnabled(true)
                                                        } catch (error: Exception) {
                                                            biometricEnabled = false
                                                            biometricMessage = UiMessage(Res.string.could_not_enable_biometric_unlock)
                                                        }
                                                    }
                                                    BiometricUnlockResult.Unavailable -> {
                                                        biometricEnabled = false
                                                        biometricMessage =
                                                            UiMessage(Res.string.set_up_fingerprint_or_face_unlock_in_system_settings)
                                                    }
                                                    is BiometricUnlockResult.Error -> {
                                                        biometricEnabled = false
                                                        biometricMessage = if (result.message.isBlank()) null else UiMessage(Res.string.authentication_failed)
                                                    }
                                                }
                                            }
                                        } else {
                                            biometricEnabled = false
                                            biometricMessage = null
                                            BiometricVaultKey.clear()
                                            BiometricSettings.setUnlockEnabled(false)
                                        }
                                    }
                                )
                            }
                        }

                        // Clipboard Clear
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
                                        text = tr(Res.string.auto_clear_clipboard),
                                        style = VaultTypography.bodyMedium,
                                        color = OnSurfacePrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = tr(Res.string.clears_secrets_after_30_seconds),
                                        style = VaultTypography.bodySmall,
                                        color = SecondarySlate,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            TactileToggleSwitch(
                                checked = autoClearClipboard,
                                onCheckedChange = { enabled ->
                                    autoClearClipboard = enabled
                                    ClipboardSettings.setAutoClearEnabled(enabled)
                                }
                            )
                        }
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("change_master_password_row")
                        .neuFlat(
                            shape = RoundedCornerShape(24.dp),
                            cornerRadius = 24.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .clickable { onChangeMasterPassword() }
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = ElectricPrimaryBright,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = tr(Res.string.change_master_password),
                                    style = VaultTypography.bodyMedium,
                                    color = OnSurfacePrimary,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = tr(Res.string.updates_the_password_that_unlocks_this_vault),
                                    style = VaultTypography.bodySmall,
                                    color = SecondarySlate,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = SecondarySlate,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sync_devices_row")
                        .neuFlat(
                            shape = RoundedCornerShape(24.dp),
                            cornerRadius = 24.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .clickable { onSyncDevices() }
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = ElectricPrimaryBright,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = tr(Res.string.sync_devices),
                                    style = VaultTypography.bodyMedium,
                                    color = OnSurfacePrimary,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = tr(Res.string.qr_or_usb_local_only_phone_must_approve),
                                    style = VaultTypography.bodySmall,
                                    color = SecondarySlate,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = SecondarySlate,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().testTag("language_setting")
                        .neuFlat(
                            shape = RoundedCornerShape(24.dp),
                            cornerRadius = 24.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .clickable { showLanguageDialog = true }
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Language, contentDescription = null,
                        tint = ElectricPrimaryBright, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(tr(Res.string.language), style = VaultTypography.bodyMedium,
                            color = OnSurfacePrimary, fontWeight = FontWeight.Medium)
                        Text(languageLabel(LanguageController.selection), style = VaultTypography.bodySmall,
                            color = SecondarySlate, fontSize = 11.sp)
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null,
                        tint = SecondarySlate, modifier = Modifier.size(22.dp))
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("manage_categories_row")
                        .neuFlat(
                            shape = RoundedCornerShape(24.dp),
                            cornerRadius = 24.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .clickable { onManageCategories() }
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = null,
                                tint = ElectricPrimaryBright,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = tr(Res.string.categories),
                                    style = VaultTypography.bodyMedium,
                                    color = OnSurfacePrimary,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = tr(Res.string.add_rename_or_remove),
                                    style = VaultTypography.bodySmall,
                                    color = SecondarySlate,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = SecondarySlate,
                            modifier = Modifier.size(22.dp)
                        )
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
                            text = tr(Res.string.lock_vault_now),
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

private fun languageLabel(language: com.example.core.localization.AppLanguage): String = when (language) {
    com.example.core.localization.AppLanguage.SYSTEM -> tr(Res.string.system_default)
    com.example.core.localization.AppLanguage.ENGLISH -> tr(Res.string.english)
    com.example.core.localization.AppLanguage.PERSIAN -> tr(Res.string.persian)
}
