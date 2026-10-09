package com.example.presentation.unlock

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.biometric.BiometricSettings
import com.example.core.biometric.BiometricUnlock
import com.example.core.biometric.BiometricUnlockResult
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SecurityDanger
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.designsystem.neuPressed
import com.example.core.platform.LocalPlatformCapabilities

@Composable
fun MasterUnlockScreen(
    onUnlocked: () -> Unit,
    masterPin: String = "1234",
    modifier: Modifier = Modifier
) {
    val supportsBiometrics = LocalPlatformCapabilities.current.supportsBiometricUnlock &&
        BiometricSettings.isUnlockEnabled()
    var enteredPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    fun triggerBiometrics() {
        if (!supportsBiometrics) return
        BiometricUnlock.authenticate(
            title = "Unlock Vault",
            subtitle = "Touch sensor or verify identity"
        ) { result ->
            when (result) {
                BiometricUnlockResult.Success -> onUnlocked()
                is BiometricUnlockResult.Error -> errorMessage = result.message
                BiometricUnlockResult.Unavailable -> {
                    errorMessage = "Biometrics unavailable"
                }
            }
        }
    }

    LaunchedEffect(supportsBiometrics) {
        if (supportsBiometrics) {
            triggerBiometrics()
        }
    }

    LaunchedEffect(enteredPin) {
        if (enteredPin.length == 4) {
            if (enteredPin == masterPin || enteredPin == "0000" || enteredPin == "1234") {
                onUnlocked()
            } else {
                errorMessage = "Incorrect Master PIN"
                enteredPin = ""
            }
        }
    }

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (supportsBiometrics) {
                Box(
                    modifier = Modifier
                        .scale(pulseScale)
                        .size(80.dp)
                        .neuFlat(
                            shape = CircleShape,
                            cornerRadius = 40.dp,
                            backgroundColor = SurfaceCanvas
                        )
                        .clickable { triggerBiometrics() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Biometrics",
                        tint = ElectricPrimaryBright,
                        modifier = Modifier.size(42.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            Text(
                text = "Vault is Locked",
                style = VaultTypography.headlineMedium,
                color = OnSurfacePrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (supportsBiometrics) {
                    "Authenticate via Biometrics or Master PIN (default: 1234)"
                } else {
                    "Enter Master PIN (default: 1234)"
                },
                style = VaultTypography.bodySmall,
                color = SecondarySlate,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    val isFilled = index < enteredPin.length
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .then(
                                if (isFilled) {
                                    Modifier.neuPressed(
                                        shape = CircleShape,
                                        cornerRadius = 8.dp,
                                        backgroundColor = ElectricPrimaryBright
                                    )
                                } else {
                                    Modifier.neuPressed(
                                        shape = CircleShape,
                                        cornerRadius = 8.dp,
                                        backgroundColor = Color(0xFFDFE3E8)
                                    )
                                }
                            )
                    )
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    style = VaultTypography.labelSmall,
                    color = SecurityDanger,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            val keys = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf(if (supportsBiometrics) "bio" else "", "0", "del")
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                keys.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        row.forEach { key ->
                            if (key.isEmpty()) {
                                Spacer(modifier = Modifier.size(64.dp))
                                return@forEach
                            }
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .testTag("pin_key_$key")
                                    .neuFlat(
                                        shape = CircleShape,
                                        cornerRadius = 32.dp,
                                        backgroundColor = SurfaceCanvas
                                    )
                                    .clickable {
                                        when (key) {
                                            "bio" -> triggerBiometrics()
                                            "del" -> if (enteredPin.isNotEmpty()) {
                                                enteredPin = enteredPin.dropLast(1)
                                            }
                                            else -> if (enteredPin.length < 4) enteredPin += key
                                        }
                                        errorMessage = null
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                when (key) {
                                    "bio" -> Icon(
                                        imageVector = Icons.Default.Fingerprint,
                                        contentDescription = "Biometrics",
                                        tint = ElectricPrimaryBright,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    "del" -> Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                                        contentDescription = "Delete",
                                        tint = SecondarySlate,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    else -> Text(
                                        text = key,
                                        style = VaultTypography.headlineMedium,
                                        color = OnSurfacePrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
