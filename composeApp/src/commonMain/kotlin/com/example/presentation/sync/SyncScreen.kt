package com.example.presentation.sync

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.sync.toImageBitmap
import com.example.core.util.rememberScreenContentPadding

@Composable
fun SyncScreen(
    viewModel: SyncViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val contentPadding = rememberScreenContentPadding(bottom = 40.dp)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .neuFlat(
                            shape = CircleShape,
                            cornerRadius = 20.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .clickable {
                            viewModel.handleIntent(SyncUiIntent.BackToIdle)
                            onBack()
                        },
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
                        text = "Sync devices",
                        style = VaultTypography.headlineLarge,
                        color = OnSurfacePrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Local only — no cloud",
                        style = VaultTypography.labelSmall,
                        color = SecondarySlate
                    )
                }
            }

            Text(
                text = "Your phone must approve every sync with biometrics or PIN. Use the same Wi‑Fi, or a USB cable with tethering on. Plugging in alone does not connect.",
                style = VaultTypography.bodySmall,
                color = SecondarySlate
            )

            if (state.canHost) {
                HostContent(
                    state = state,
                    onStartHost = { viewModel.handleIntent(SyncUiIntent.StartHosting) },
                    onStopHost = { viewModel.handleIntent(SyncUiIntent.StopHosting) },
                    onRefreshQr = { viewModel.handleIntent(SyncUiIntent.RefreshQr) }
                )
            }
            if (state.canScanQr) {
                ScanContent(
                    state = state,
                    onQrScanned = { viewModel.handleIntent(SyncUiIntent.QrScanned(it)) },
                    onManualChanged = { viewModel.handleIntent(SyncUiIntent.ManualUriChanged(it)) },
                    onConnectManual = { viewModel.handleIntent(SyncUiIntent.ConnectWithManualUri) }
                )
            }

            when (val phase = state.phase) {
                is SyncPhase.Error -> {
                    Text(
                        text = phase.message,
                        style = VaultTypography.bodyMedium,
                        color = androidx.compose.ui.graphics.Color(0xFFBA1A1A),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ActionButton(
                        label = "Retry",
                        onClick = { viewModel.handleIntent(SyncUiIntent.RetryScan) }
                    )
                }
                else -> PhaseBanner(phase)
            }

            if (state.pinFallbackVisible) {
                PinFallbackCard(
                    pin = state.pinInput,
                    onPinChanged = { viewModel.handleIntent(SyncUiIntent.PinChanged(it)) },
                    onSubmit = { viewModel.handleIntent(SyncUiIntent.SubmitPinFallback) },
                    onCancel = { viewModel.handleIntent(SyncUiIntent.DismissPinFallback) }
                )
            }
        }
    }
}

@Composable
private fun HostContent(
    state: SyncUiState,
    onStartHost: () -> Unit,
    onStopHost: () -> Unit,
    onRefreshQr: () -> Unit
) {
    val hosting = state.phase is SyncPhase.Hosting
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ActionButton(
            label = if (hosting) "Stop waiting" else "Show QR & wait",
            onClick = if (hosting) onStopHost else onStartHost
        )
        val qr = state.qrImage
        if (hosting && qr != null) {
            val bitmap = remember(qr) { qr.toImageBitmap() }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neuFlat(
                        shape = RoundedCornerShape(20.dp),
                        cornerRadius = 20.dp,
                        backgroundColor = SurfaceContainerLowest
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Sync QR code",
                    modifier = Modifier.size(220.dp)
                )
            }
            val advertised = state.lanAddresses.filter { it != "127.0.0.1" }
            Text(
                text = if (advertised.isEmpty()) {
                    "No network address yet. Turn on Wi‑Fi or USB tethering, then refresh."
                } else {
                    "Addresses: ${advertised.joinToString()}"
                },
                style = VaultTypography.bodySmall,
                color = SecondarySlate
            )
            ActionButton(label = "Refresh QR", onClick = onRefreshQr)
        }
    }
}

@Composable
private fun ScanContent(
    state: SyncUiState,
    onQrScanned: (String) -> Unit,
    onManualChanged: (String) -> Unit,
    onConnectManual: () -> Unit
) {
    val scanning = state.phase is SyncPhase.Idle
    val canPaste = state.phase is SyncPhase.Idle || state.phase is SyncPhase.Error
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (scanning) {
            Text(
                text = "Point the camera at the QR on your computer",
                style = VaultTypography.bodySmall,
                color = SecondarySlate
            )
            key(state.scanEpoch) {
                SyncQrCamera(onResult = onQrScanned)
            }
        }
        if (canPaste) {
            ManualUriField(
                value = state.manualUri,
                onChanged = onManualChanged,
                onConnect = onConnectManual,
                hint = "Or paste passilius://sync?... from the computer"
            )
        }
    }
}

@Composable
private fun ManualUriField(
    value: String,
    onChanged: (String) -> Unit,
    onConnect: () -> Unit,
    hint: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neuFlat(
                shape = RoundedCornerShape(20.dp),
                cornerRadius = 20.dp,
                backgroundColor = SurfaceContainerLowest
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = hint, style = VaultTypography.labelSmall, color = SecondarySlate)
        BasicTextField(
            value = value,
            onValueChange = onChanged,
            textStyle = VaultTypography.bodySmall.copy(color = OnSurfacePrimary),
            cursorBrush = SolidColor(ElectricPrimaryBright),
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        )
        ActionButton(label = "Connect & approve", onClick = onConnect)
    }
}

@Composable
private fun PinFallbackCard(
    pin: String,
    onPinChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neuFlat(
                shape = RoundedCornerShape(20.dp),
                cornerRadius = 20.dp,
                backgroundColor = SurfaceContainerLowest
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Enter the master password to allow sync",
            style = VaultTypography.bodyMedium,
            color = OnSurfacePrimary,
            fontWeight = FontWeight.Medium
        )
        BasicTextField(
            value = pin,
            onValueChange = onPinChanged,
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
            textStyle = VaultTypography.headlineSmall.copy(color = OnSurfacePrimary),
            cursorBrush = SolidColor(ElectricPrimaryBright),
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActionButton(label = "Allow sync", onClick = onSubmit, modifier = Modifier.weight(1f))
            ActionButton(label = "Cancel", onClick = onCancel, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun PhaseBanner(phase: SyncPhase) {
    val (text, emphasize) = when (phase) {
        SyncPhase.Idle -> return
        SyncPhase.Hosting -> "Waiting for phone…" to false
        SyncPhase.AwaitingAuth -> "Waiting for biometric / PIN approval…" to false
        SyncPhase.Transferring -> "Transferring encrypted vault…" to false
        is SyncPhase.Done -> phase.message to false
        is SyncPhase.Error -> phase.message to true
    }
    Text(
        text = text,
        style = VaultTypography.bodyMedium,
        color = if (emphasize) androidx.compose.ui.graphics.Color(0xFFBA1A1A) else ElectricPrimaryBright,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .neuFlat(
                shape = RoundedCornerShape(18.dp),
                cornerRadius = 18.dp,
                backgroundColor = SurfaceContainerLowest
            )
            .clickable(onClick = onClick),
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
