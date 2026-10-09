package com.example.presentation.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.biometric.BiometricUnlock
import com.example.core.biometric.BiometricUnlockResult
import com.example.core.crypto.VaultKeyStore
import com.example.core.platform.platformCapabilities
import com.example.core.sync.QrCodec
import com.example.core.sync.SyncClient
import com.example.core.sync.SyncHost
import com.example.domain.sync.SyncPairingInfo
import com.example.domain.usecase.BuildVaultSnapshotUseCase
import com.example.domain.usecase.MergeVaultSnapshotUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class SyncViewModel(
    private val buildSnapshot: BuildVaultSnapshotUseCase,
    private val mergeSnapshot: MergeVaultSnapshotUseCase,
    private val syncHost: SyncHost = SyncHost(),
    private val syncClient: SyncClient = SyncClient()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SyncUiState(
            canHost = syncHost.isSupported,
            canScanQr = platformCapabilities().canScanSyncQr
        )
    )
    val uiState: StateFlow<SyncUiState> = _uiState.asStateFlow()

    private var hostTimeoutJob: Job? = null
    private var pendingPairing: SyncPairingInfo? = null

    fun handleIntent(intent: SyncUiIntent) {
        when (intent) {
            SyncUiIntent.BackToIdle -> resetToIdle()
            SyncUiIntent.StartHosting -> startHosting()
            SyncUiIntent.StopHosting -> stopHosting()
            SyncUiIntent.RefreshQr -> refreshQr()
            SyncUiIntent.RetryScan -> retryScan()
            is SyncUiIntent.QrScanned -> beginClientAuth(intent.uri)
            is SyncUiIntent.ManualUriChanged -> _uiState.update { it.copy(manualUri = intent.value) }
            SyncUiIntent.ConnectWithManualUri -> beginClientAuth(_uiState.value.manualUri)
            SyncUiIntent.ConfirmAuthAndSync -> requestPhoneAuthThenSync()
            SyncUiIntent.AuthCancelled -> {
                pendingPairing = null
                _uiState.update {
                    it.copy(phase = SyncPhase.Error("Sync cancelled — no data was sent."))
                }
            }
            is SyncUiIntent.PinChanged -> _uiState.update { it.copy(pinInput = intent.value) }
            SyncUiIntent.SubmitPinFallback -> submitMasterPassword()
            SyncUiIntent.DismissPinFallback -> {
                pendingPairing = null
                _uiState.update {
                    it.copy(
                        pinFallbackVisible = false,
                        pinInput = "",
                        phase = SyncPhase.Error("Sync cancelled — no data was sent.")
                    )
                }
            }
        }
    }

    private fun resetToIdle() {
        viewModelScope.launch { syncHost.stop() }
        hostTimeoutJob?.cancel()
        pendingPairing = null
        _uiState.update {
            SyncUiState(
                canHost = syncHost.isSupported,
                canScanQr = platformCapabilities().canScanSyncQr,
                scanEpoch = it.scanEpoch + 1
            )
        }
    }

    private fun retryScan() {
        pendingPairing = null
        _uiState.update {
            it.copy(
                phase = SyncPhase.Idle,
                pinFallbackVisible = false,
                pinInput = "",
                scanEpoch = it.scanEpoch + 1
            )
        }
    }

    private fun refreshQr() {
        val state = _uiState.value
        if (state.phase !is SyncPhase.Hosting || state.pairingUri.isBlank()) return
        val pairing = SyncPairingInfo.parseUri(state.pairingUri) ?: return
        val hosts = syncHost.lanAddresses()
        if (hosts.isEmpty()) return
        val uri = pairing.withHosts(hosts).toUri()
        val qr = QrCodec.encode(uri, sizePx = 512)
        _uiState.update {
            it.copy(
                pairingUri = uri,
                lanAddresses = hosts,
                qrImage = qr
            )
        }
    }

    private fun startHosting() {
        if (!syncHost.isSupported) {
            _uiState.update {
                it.copy(phase = SyncPhase.Error("Hosting sync is only available on Windows desktop."))
            }
            return
        }
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(phase = SyncPhase.Hosting) }
                val session = syncHost.start { incoming ->
                    val remoteMerge = mergeSnapshot(incoming)
                    val response = buildSnapshot(incoming.sessionId)
                    hostTimeoutJob?.cancel()
                    _uiState.update {
                        it.copy(
                            phase = SyncPhase.Done("Sync complete. ${remoteMerge.summary}"),
                            lastRemoteMerge = remoteMerge,
                            qrImage = null
                        )
                    }
                    viewModelScope.launch {
                        delay(300)
                        syncHost.stop()
                    }
                    remoteMerge to response
                }
                val uri = session.pairing.toUri()
                val qr = QrCodec.encode(uri, sizePx = 512)
                _uiState.update {
                    it.copy(
                        phase = SyncPhase.Hosting,
                        pairingUri = uri,
                        lanAddresses = session.lanAddresses,
                        qrImage = qr,
                        lastRemoteMerge = null,
                        lastLocalMerge = null
                    )
                }
                hostTimeoutJob?.cancel()
                hostTimeoutJob = viewModelScope.launch {
                    delay(120_000)
                    syncHost.stop()
                    _uiState.update {
                        if (it.phase is SyncPhase.Hosting) {
                            it.copy(phase = SyncPhase.Error("Sync session timed out."))
                        } else it
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(phase = SyncPhase.Error(e.message ?: "Failed to start sync host"))
                }
            }
        }
    }

    private fun stopHosting() {
        viewModelScope.launch {
            syncHost.stop()
            hostTimeoutJob?.cancel()
            _uiState.update {
                it.copy(
                    phase = SyncPhase.Idle,
                    pairingUri = "",
                    qrImage = null,
                    lanAddresses = emptyList()
                )
            }
        }
    }

    private fun submitMasterPassword() {
        val password = _uiState.value.pinInput
        viewModelScope.launch {
            val opened = VaultKeyStore.unlock(password)
            if (opened) {
                _uiState.update { it.copy(pinFallbackVisible = false, pinInput = "") }
                runClientSync()
            } else {
                _uiState.update {
                    it.copy(
                        phase = SyncPhase.Error("Incorrect master password"),
                        pinInput = ""
                    )
                }
            }
        }
    }

    private fun beginClientAuth(uri: String) {
        val pairing = SyncPairingInfo.parseUri(uri)
        if (pairing == null) {
            _uiState.update { it.copy(phase = SyncPhase.Error("Invalid sync code / QR")) }
            return
        }
        pendingPairing = pairing
        _uiState.update { it.copy(phase = SyncPhase.AwaitingAuth) }
        requestPhoneAuthThenSync()
    }

    private fun requestPhoneAuthThenSync() {
        if (pendingPairing == null) return
        viewModelScope.launch {
            _uiState.update { it.copy(phase = SyncPhase.AwaitingAuth) }
            when (val result = authenticateForSync()) {
                BiometricUnlockResult.Success -> runClientSync()
                BiometricUnlockResult.Unavailable -> {
                    _uiState.update { it.copy(pinFallbackVisible = true) }
                }
                is BiometricUnlockResult.Error -> {
                    pendingPairing = null
                    _uiState.update {
                        it.copy(phase = SyncPhase.Error(result.message.ifBlank { "Authentication failed" }))
                    }
                }
            }
        }
    }

    private suspend fun authenticateForSync(): BiometricUnlockResult {
        if (!BiometricUnlock.isAvailable()) return BiometricUnlockResult.Unavailable
        return suspendCancellableCoroutine { cont ->
            BiometricUnlock.authenticate(
                title = "Confirm sync",
                subtitle = "Verify it's you before sharing the vault with this computer"
            ) { result ->
                if (cont.isActive) cont.resume(result)
            }
        }
    }

    private fun runClientSync() {
        val pairing = pendingPairing ?: return
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(phase = SyncPhase.Transferring) }
                val local = buildSnapshot(pairing.sessionId)
                val remote = syncClient.exchange(pairing, local)
                val localMerge = mergeSnapshot(remote)
                pendingPairing = null
                _uiState.update {
                    it.copy(
                        phase = SyncPhase.Done("Sync complete. ${localMerge.summary}"),
                        lastLocalMerge = localMerge
                    )
                }
            } catch (e: Exception) {
                pendingPairing = null
                _uiState.update {
                    it.copy(phase = SyncPhase.Error(e.message ?: "Sync failed"))
                }
            }
        }
    }

    override fun onCleared() {
        hostTimeoutJob?.cancel()
        viewModelScope.launch { syncHost.stop() }
        super.onCleared()
    }
}
