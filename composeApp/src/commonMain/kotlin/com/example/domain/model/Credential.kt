package com.example.domain.model

import com.example.core.platform.currentTimeMillis

data class Credential(
    val id: Long = 0,
    val service: String,
    val username: String,
    val password: String, // Plaintext when decrypted in domain layer
    val category: String = "Personal",
    val websiteUrl: String = "",
    val notes: String = "",
    val iconKey: String = "",
    val createdAt: Long = currentTimeMillis(),
    val updatedAt: Long = currentTimeMillis(),
    val isFavorite: Boolean = false,
    val entropyBits: Int = 0
)

data class VaultCategory(
    val id: Long = 0,
    val name: String
)

data class SecurityReport(
    val totalCount: Int = 0,
    val strongCount: Int = 0,
    val weakCount: Int = 0,
    val reusedCount: Int = 0,
    val securityScorePercentage: Int = 100
)
