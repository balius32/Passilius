package com.example.core.sync

import com.example.domain.sync.MergeResult
import com.example.domain.sync.SyncPairingInfo
import com.example.domain.sync.VaultSnapshot

actual class SyncHost {
    actual val isSupported: Boolean = false

    actual fun lanAddresses(): List<String> = emptyList()

    actual suspend fun start(
        onIncoming: suspend (VaultSnapshot) -> Pair<MergeResult, VaultSnapshot>
    ): SyncHostSession {
        error("Sync hosting is only available on desktop")
    }

    actual suspend fun stop() = Unit
}

actual class SyncClient {
    actual suspend fun exchange(
        pairing: SyncPairingInfo,
        localSnapshot: VaultSnapshot
    ): VaultSnapshot = exchangeSnapshot(pairing, localSnapshot)
}
