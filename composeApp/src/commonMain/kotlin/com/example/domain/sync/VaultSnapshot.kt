package com.example.domain.sync

import kotlinx.serialization.Serializable

@Serializable
data class VaultSnapshot(
    val formatVersion: Int = 1,
    val sessionId: String,
    val exportedAt: Long,
    val categories: List<CategorySnapshot> = emptyList(),
    val credentials: List<CredentialSnapshot> = emptyList()
)

@Serializable
data class CategorySnapshot(
    val syncId: String,
    val name: String,
    val sortOrder: Int
)

@Serializable
data class CredentialSnapshot(
    val syncId: String,
    val service: String,
    val username: String,
    val password: String,
    val categorySyncId: String,
    val websiteUrl: String = "",
    val notes: String = "",
    val iconKey: String = "",
    val createdAt: Long,
    val updatedAt: Long,
    val isFavorite: Boolean = false,
    val entropyBits: Int = 0
)

data class MergeResult(
    val categoriesAdded: Int = 0,
    val categoriesUpdated: Int = 0,
    val credentialsAdded: Int = 0,
    val credentialsUpdated: Int = 0,
    val credentialsSkipped: Int = 0
) {
    val summary: String
        get() = buildString {
            append("Credentials +$credentialsAdded / ~$credentialsUpdated")
            if (credentialsSkipped > 0) append(" / skipped $credentialsSkipped")
            append(". Categories +$categoriesAdded / ~$categoriesUpdated")
        }
}
