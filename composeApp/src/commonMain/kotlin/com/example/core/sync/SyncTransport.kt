package com.example.core.sync

import com.example.domain.sync.MergeResult
import com.example.domain.sync.SyncPairingInfo
import com.example.domain.sync.VaultSnapshot

data class SyncHostSession(
    val pairing: SyncPairingInfo,
    val lanAddresses: List<String>
)

data class SyncExchangeResult(
    val remoteMerge: MergeResult,
    val localMerge: MergeResult
)

expect class SyncHost() {
    val isSupported: Boolean
    suspend fun start(
        onIncoming: suspend (VaultSnapshot) -> Pair<MergeResult, VaultSnapshot>
    ): SyncHostSession

    suspend fun stop()
}

expect class SyncClient() {
    suspend fun exchange(
        pairing: SyncPairingInfo,
        localSnapshot: VaultSnapshot
    ): VaultSnapshot
}
