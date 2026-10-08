package com.example.domain.usecase

import com.example.core.platform.currentTimeMillis
import com.example.core.platform.newSyncId
import com.example.domain.repository.VaultRepository
import com.example.domain.sync.CategorySnapshot
import com.example.domain.sync.CredentialSnapshot
import com.example.domain.sync.MergeResult
import com.example.domain.sync.VaultSnapshot

class BuildVaultSnapshotUseCase(
    private val repository: VaultRepository
) {
    suspend operator fun invoke(sessionId: String = newSyncId()): VaultSnapshot {
        val categories = repository.listCategories()
        val nameToSyncId = categories.associate { it.name to it.syncId.ifBlank { newSyncId() } }
        val categorySnapshots = categories.map {
            CategorySnapshot(
                syncId = it.syncId.ifBlank { nameToSyncId.getValue(it.name) },
                name = it.name,
                sortOrder = it.sortOrder
            )
        }
        val credentials = repository.listCredentials().map { cred ->
            val categorySyncId = nameToSyncId[cred.category]
                ?: categorySnapshots.firstOrNull { it.name == cred.category }?.syncId
                ?: newSyncId()
            CredentialSnapshot(
                syncId = cred.syncId.ifBlank { newSyncId() },
                service = cred.service,
                username = cred.username,
                password = cred.password,
                categorySyncId = categorySyncId,
                websiteUrl = cred.websiteUrl,
                notes = cred.notes,
                iconKey = cred.iconKey,
                createdAt = cred.createdAt,
                updatedAt = cred.updatedAt,
                isFavorite = cred.isFavorite,
                entropyBits = cred.entropyBits
            )
        }
        return VaultSnapshot(
            sessionId = sessionId,
            exportedAt = currentTimeMillis(),
            categories = categorySnapshots,
            credentials = credentials
        )
    }
}

class MergeVaultSnapshotUseCase(
    private val repository: VaultRepository
) {
    suspend operator fun invoke(snapshot: VaultSnapshot): MergeResult =
        repository.mergeSnapshot(snapshot)
}
