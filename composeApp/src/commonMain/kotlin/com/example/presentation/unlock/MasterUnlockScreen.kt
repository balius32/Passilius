package com.example.presentation.unlock

import com.example.core.localization.*

import com.example.core.localization.tr
import passilius.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.core.biometric.BiometricSettings
import com.example.core.biometric.BiometricUnlock
import com.example.core.biometric.BiometricUnlockResult
import com.example.core.crypto.BiometricVaultKey
import com.example.core.crypto.VaultKeyStore
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SecurityDanger
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.platform.LocalPlatformCapabilities

@Composable
fun MasterUnlockScreen(
    needsSetup: Boolean,
    isUnlocking: Boolean,
    errorMessage: String?,
    onCreateMasterPassword: (password: String, confirm: String) -> Unit,
    onSubmitMasterPassword: (password: String) -> Unit,
    onBiometricUnlocked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val supportsBiometrics = LocalPlatformCapabilities.current.supportsBiometricUnlock &&
        BiometricSettings.isUnlockEnabled() &&
        BiometricVaultKey.isEnrolled()
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<UiMessage?>(null) }

    fun triggerBiometrics() {
        if (!supportsBiometrics || needsSetup) return
        BiometricUnlock.authenticate(
            title = tr(Res.string.unlock_vault),
            subtitle = tr(Res.string.touch_sensor_or_verify_identity)
        ) { result ->
            when (result) {
                BiometricUnlockResult.Success -> onBiometricUnlocked()
                is BiometricUnlockResult.Error -> localError = if (result.message.isBlank()) null else UiMessage(Res.string.authentication_failed)
                BiometricUnlockResult.Unavailable -> localError = UiMessage(Res.string.biometrics_unavailable)
            }
        }
    }

    LaunchedEffect(supportsBiometrics, needsSetup) {
        if (supportsBiometrics && !needsSetup) triggerBiometrics()
    }

    val shownError = errorMessage ?: localError?.resolve()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (needsSetup) tr(Res.string.create_master_password) else tr(Res.string.unlock_vault),
                style = VaultTypography.headlineLarge,
                color = OnSurfacePrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (needsSetup) {
                    tr(Res.string.setup_hint, VaultKeyStore.MIN_PASSWORD_LENGTH)
                } else {
                    tr(Res.string.enter_the_master_password_that_encrypts_this_vault)
                },
                style = VaultTypography.bodySmall,
                color = SecondarySlate
            )
            PasswordField(
                value = password,
                onValueChange = {
                    password = it
                    localError = null
                },
                hint = tr(Res.string.master_password)
            )
            if (needsSetup) {
                PasswordField(
                    value = confirm,
                    onValueChange = {
                        confirm = it
                        localError = null
                    },
                    hint = tr(Res.string.confirm_password)
                )
            }
            if (!shownError.isNullOrBlank()) {
                Text(
                    text = shownError,
                    style = VaultTypography.bodySmall,
                    color = SecurityDanger
                )
            }
            UnlockButton(
                label = when {
                    isUnlocking -> tr(Res.string.working)
                    needsSetup -> tr(Res.string.create_vault)
                    else -> tr(Res.string.unlock)
                },
                enabled = !isUnlocking,
                onClick = {
                    if (needsSetup) onCreateMasterPassword(password, confirm)
                    else onSubmitMasterPassword(password)
                }
            )
            if (supportsBiometrics && !needsSetup) {
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = tr(Res.string.unlock_with_biometrics),
                    tint = ElectricPrimaryBright,
                    modifier = Modifier.clickable(enabled = !isUnlocking) { triggerBiometrics() }
                )
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        Text(text = hint, style = VaultTypography.labelSmall, color = SecondarySlate)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .neuFlat(
                    shape = RoundedCornerShape(18.dp),
                    cornerRadius = 18.dp,
                    backgroundColor = SurfaceContainerLowest
                )
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                textStyle = VaultTypography.bodyMedium.copy(color = OnSurfacePrimary, textDirection = androidx.compose.ui.text.style.TextDirection.ContentOrLtr),
                cursorBrush = SolidColor(ElectricPrimaryBright),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 6.dp)
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { visible = !visible },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) tr(Res.string.hide_field, hint) else tr(Res.string.show_field, hint),
                    tint = SecondarySlate,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun UnlockButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .neuFlat(
                shape = RoundedCornerShape(18.dp),
                cornerRadius = 18.dp,
                backgroundColor = SurfaceContainerLowest
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = VaultTypography.bodyMedium,
            color = ElectricPrimaryBright,
            fontWeight = FontWeight.SemiBold
        )
    }
}
