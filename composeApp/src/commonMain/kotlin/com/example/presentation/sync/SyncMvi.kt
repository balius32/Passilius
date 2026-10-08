package com.example.presentation.sync

import com.example.core.sync.QrImage
import com.example.domain.sync.MergeResult

enum class SyncTransportMode {
    Qr,
    Usb
}

sealed interface SyncPhase {
    data object Idle : SyncPhase
    data object Hosting : SyncPhase
    data object AwaitingAuth : SyncPhase
    data object Transferring : SyncPhase
    data class Done(val message: String) : SyncPhase
    data class Error(val message: String) : SyncPhase
}

data class SyncUiState(
    val mode: SyncTransportMode? = null,
    val phase: SyncPhase = SyncPhase.Idle,
    val canHost: Boolean = false,
    val canScanQr: Boolean = false,
    val pairingUri: String = "",
    val lanAddresses: List<String> = emptyList(),
    val qrImage: QrImage? = null,
    val manualUri: String = "",
    val lastLocalMerge: MergeResult? = null,
    val lastRemoteMerge: MergeResult? = null,
    val pinFallbackVisible: Boolean = false,
    val pinInput: String = ""
)

sealed interface SyncUiIntent {
    data object BackToIdle : SyncUiIntent
    data class SelectMode(val mode: SyncTransportMode) : SyncUiIntent
    data object StartHosting : SyncUiIntent
    data object StopHosting : SyncUiIntent
    data object ScanQr : SyncUiIntent
    data class QrScanned(val uri: String) : SyncUiIntent
    data class ManualUriChanged(val value: String) : SyncUiIntent
    data object ConnectWithManualUri : SyncUiIntent
    data object ConfirmAuthAndSync : SyncUiIntent
    data object AuthCancelled : SyncUiIntent
    data class PinChanged(val value: String) : SyncUiIntent
    data object SubmitPinFallback : SyncUiIntent
    data object DismissPinFallback : SyncUiIntent
}
