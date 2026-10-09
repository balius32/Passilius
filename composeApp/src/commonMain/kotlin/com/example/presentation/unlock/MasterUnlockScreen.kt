package com.example.presentation.unlock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Fingerprint
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    var localError by remember { mutableStateOf<String?>(null) }

    fun triggerBiometrics() {
        if (!supportsBiometrics || needsSetup) return
        BiometricUnlock.authenticate(
            title = "Unlock Vault",
            subtitle = "Touch sensor or verify identity"
        ) { result ->
            when (result) {
                BiometricUnlockResult.Success -> onBiometricUnlocked()
                is BiometricUnlockResult.Error -> localError = result.message.ifBlank { null }
                BiometricUnlockResult.Unavailable -> localError = "Biometrics unavailable"
            }
        }
    }

    LaunchedEffect(supportsBiometrics, needsSetup) {
        if (supportsBiometrics && !needsSetup) triggerBiometrics()
    }

    val shownError = errorMessage ?: localError

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
                text = if (needsSetup) "Create master password" else "Unlock vault",
                style = VaultTypography.headlineLarge,
                color = OnSurfacePrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (needsSetup) {
                    "At least ${VaultKeyStore.MIN_PASSWORD_LENGTH} characters. This password encrypts the vault."
                } else {
                    "Enter the master password that encrypts this vault."
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
                hint = "Master password"
            )
            if (needsSetup) {
                PasswordField(
                    value = confirm,
                    onValueChange = {
                        confirm = it
                        localError = null
                    },
                    hint = "Confirm password"
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
                    isUnlocking -> "Working…"
                    needsSetup -> "Create vault"
                    else -> "Unlock"
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
                    contentDescription = "Unlock with biometrics",
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
    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        Text(text = hint, style = VaultTypography.labelSmall, color = SecondarySlate)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            textStyle = VaultTypography.bodyMedium.copy(color = OnSurfacePrimary),
            cursorBrush = SolidColor(ElectricPrimaryBright),
            modifier = Modifier
                .fillMaxWidth()
                .neuFlat(
                    shape = RoundedCornerShape(18.dp),
                    cornerRadius = 18.dp,
                    backgroundColor = SurfaceContainerLowest
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )
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
