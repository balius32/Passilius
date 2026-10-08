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
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val launchScanner = rememberSyncQrScanner(
        onResult = { viewModel.handleIntent(SyncUiIntent.QrScanned(it)) }
    )

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
                text = "Your phone must approve every sync with biometrics or PIN. Plugging in a cable does nothing by itself.",
                style = VaultTypography.bodySmall,
                color = SecondarySlate
            )

            when {
                state.mode == null -> ModePicker(
                    canHost = state.canHost,
                    onSelect = { viewModel.handleIntent(SyncUiIntent.SelectMode(it)) }
                )
                else -> {
                    ModeHeader(
                        mode = state.mode!!,
                        onChange = { viewModel.handleIntent(SyncUiIntent.BackToIdle) }
                    )
                    when (val mode = state.mode) {
                        SyncTransportMode.Qr -> QrModeContent(
                            state = state,
                            canHost = state.canHost,
                            canScan = state.canScanQr,
                            onStartHost = { viewModel.handleIntent(SyncUiIntent.StartHosting) },
                            onStopHost = { viewModel.handleIntent(SyncUiIntent.StopHosting) },
                            onScan = {
                                if (state.canScanQr) launchScanner()
                            },
                            onManualChanged = {
                                viewModel.handleIntent(SyncUiIntent.ManualUriChanged(it))
                            },
                            onConnectManual = {
                                viewModel.handleIntent(SyncUiIntent.ConnectWithManualUri)
                            }
                        )
                        SyncTransportMode.Usb -> UsbModeContent(
                            state = state,
                            canHost = state.canHost,
                            onStartHost = { viewModel.handleIntent(SyncUiIntent.StartHosting) },
                            onStopHost = { viewModel.handleIntent(SyncUiIntent.StopHosting) },
                            onManualChanged = {
                                viewModel.handleIntent(SyncUiIntent.ManualUriChanged(it))
                            },
                            onConnectManual = {
                                viewModel.handleIntent(SyncUiIntent.ConnectWithManualUri)
                            }
                        )
                        null -> error("unreachable: mode=$mode")
                    }
                }
            }

            PhaseBanner(state.phase)

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
private fun ModePicker(
    canHost: Boolean,
    onSelect: (SyncTransportMode) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SyncOptionCard(
            title = "Sync via QR",
            subtitle = if (canHost) {
                "Show a QR on this PC — scan it from your phone on the same Wi‑Fi"
            } else {
                "Scan the QR shown on your Windows app"
            },
            icon = { Icon(Icons.Default.QrCode2, null, tint = ElectricPrimaryBright) },
            onClick = { onSelect(SyncTransportMode.Qr) }
        )
        SyncOptionCard(
            title = "Sync via USB",
            subtitle = if (canHost) {
                "Enable USB tethering on the phone, then wait for the phone to connect"
            } else {
                "Plug in, enable USB tethering, approve with biometrics, then connect"
            },
            icon = { Icon(Icons.Default.Usb, null, tint = ElectricPrimaryBright) },
            onClick = { onSelect(SyncTransportMode.Usb) }
        )
    }
}

@Composable
private fun ModeHeader(mode: SyncTransportMode, onChange: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (mode) {
                SyncTransportMode.Qr -> "QR (Wi‑Fi)"
                SyncTransportMode.Usb -> "USB cable"
            },
            style = VaultTypography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = OnSurfacePrimary
        )
        Text(
            text = "Change",
            style = VaultTypography.labelSmall,
            color = ElectricPrimaryBright,
            modifier = Modifier.clickable(onClick = onChange)
        )
    }
}

@Composable
private fun QrModeContent(
    state: SyncUiState,
    canHost: Boolean,
    canScan: Boolean,
    onStartHost: () -> Unit,
    onStopHost: () -> Unit,
    onScan: () -> Unit,
    onManualChanged: (String) -> Unit,
    onConnectManual: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (canHost) {
            ActionButton(
                label = if (state.phase is SyncPhase.Hosting) "Stop waiting" else "Show QR & wait",
                onClick = if (state.phase is SyncPhase.Hosting) onStopHost else onStartHost
            )
            if (state.qrImage != null) {
                val bitmap = remember(state.qrImage) { state.qrImage.toImageBitmap() }
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
                Text(
                    text = "Addresses: ${state.lanAddresses.joinToString()}",
                    style = VaultTypography.bodySmall,
                    color = SecondarySlate
                )
                if (state.pairingUri.isNotBlank()) {
                    Text(
                        text = state.pairingUri,
                        style = VaultTypography.labelSmall,
                        color = SecondarySlate,
                        fontSize = 10.sp
                    )
                }
            }
        } else {
            if (canScan) {
                ActionButton(label = "Scan QR code", onClick = onScan)
            }
            ManualUriField(
                value = state.manualUri,
                onChanged = onManualChanged,
                onConnect = onConnectManual,
                hint = "Or paste passilius://sync?... from desktop"
            )
        }
    }
}

@Composable
private fun UsbModeContent(
    state: SyncUiState,
    canHost: Boolean,
    onStartHost: () -> Unit,
    onStopHost: () -> Unit,
    onManualChanged: (String) -> Unit,
    onConnectManual: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InstructionCard(
            if (canHost) {
                listOf(
                    "1. Plug the phone into this PC with a USB cable.",
                    "2. On the phone, enable USB tethering (Hotspot & tethering).",
                    "3. Tap “Start USB sync host” below — allow the Windows firewall if asked.",
                    "4. On the phone: Sync devices → USB → approve biometrics → Connect.",
                    "5. Nothing is shared until the phone approves."
                )
            } else {
                listOf(
                    "1. Plug into the PC with a USB cable.",
                    "2. Enable USB tethering on this phone.",
                    "3. On Windows Passilius: Sync → USB → Start USB sync host.",
                    "4. Paste the sync link shown on the PC (or scan QR if on tether network).",
                    "5. Approve with biometrics/PIN — only then is the vault sent."
                )
            }
        )
        if (canHost) {
            ActionButton(
                label = if (state.phase is SyncPhase.Hosting) "Stop USB host" else "Start USB sync host",
                onClick = if (state.phase is SyncPhase.Hosting) onStopHost else onStartHost
            )
            if (state.phase is SyncPhase.Hosting) {
                Text(
                    text = "Waiting… IPs: ${state.lanAddresses.joinToString()}\nPhone must connect after biometric approval.",
                    style = VaultTypography.bodySmall,
                    color = SecondarySlate
                )
                if (state.pairingUri.isNotBlank()) {
                    Text(
                        text = "Paste on phone:\n${state.pairingUri}",
                        style = VaultTypography.labelSmall,
                        color = OnSurfacePrimary,
                        fontSize = 10.sp
                    )
                }
                if (state.qrImage != null) {
                    val bitmap = remember(state.qrImage) { state.qrImage.toImageBitmap() }
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Sync QR for USB tether",
                        modifier = Modifier
                            .size(180.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        } else {
            ManualUriField(
                value = state.manualUri,
                onChanged = onManualChanged,
                onConnect = onConnectManual,
                hint = "Paste passilius://sync?... from the PC"
            )
        }
    }
}

@Composable
private fun InstructionCard(lines: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neuFlat(
                shape = RoundedCornerShape(20.dp),
                cornerRadius = 20.dp,
                backgroundColor = SurfaceContainerLowest
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        lines.forEach { line ->
            Text(
                text = line,
                style = VaultTypography.bodySmall,
                color = OnSurfacePrimary
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
            text = "Biometrics unavailable — enter master PIN to allow sync",
            style = VaultTypography.bodyMedium,
            color = OnSurfacePrimary,
            fontWeight = FontWeight.Medium
        )
        BasicTextField(
            value = pin,
            onValueChange = { if (it.length <= 8) onPinChanged(it.filter { c -> c.isDigit() }) },
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
private fun SyncOptionCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .neuFlat(
                shape = RoundedCornerShape(22.dp),
                cornerRadius = 22.dp,
                backgroundColor = SurfaceContainerLowest
            )
            .clickable(onClick = onClick)
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .neuFlat(
                    shape = CircleShape,
                    cornerRadius = 22.dp,
                    backgroundColor = SurfaceCanvas
                ),
            contentAlignment = Alignment.Center
        ) { icon() }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = VaultTypography.bodyMedium,
                color = OnSurfacePrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = VaultTypography.bodySmall,
                color = SecondarySlate,
                fontSize = 11.sp
            )
        }
        Icon(Icons.Default.Sync, null, tint = SecondarySlate, modifier = Modifier.size(20.dp))
    }
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
