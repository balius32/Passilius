package com.example.domain.model

data class Credential(
    val id: Long = 0,
    val service: String,
    val username: String,
    val password: String, // Plaintext when decrypted in domain layer
    val category: String = "Personal",
    val websiteUrl: String = "",
    val totpSecret: String = "",
    val notes: String = "",
    val iconKey: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val entropyBits: Int = 0
)

data class VaultCategory(
    val id: String,
    val name: String,
    val iconName: String
)

data class SecurityReport(
    val totalCount: Int = 0,
    val strongCount: Int = 0,
    val weakCount: Int = 0,
    val reusedCount: Int = 0,
    val with2faCount: Int = 0,
    val securityScorePercentage: Int = 100
)
