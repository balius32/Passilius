package com.example.domain.model

import com.example.core.crypto.PasswordGenerator
import com.example.core.crypto.PasswordStrengthLevel
import com.example.core.platform.currentTimeMillis

data class Credential(
    val id: Long = 0,
    val syncId: String = "",
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
    val syncId: String = "",
    val name: String,
    val sortOrder: Int = 0
)

data class SecurityReport(
    val totalCount: Int = 0,
    val strongCount: Int = 0,
    val weakCount: Int = 0,
    val reusedCount: Int = 0,
    val securityScorePercentage: Int = 0,
    val gradeLabel: String = "Empty"
) {
    companion object {
        fun from(credentials: List<Credential>): SecurityReport {
            if (credentials.isEmpty()) return SecurityReport()

            val passwordCounts = credentials.groupingBy { it.password }.eachCount()
            var strongCount = 0
            var weakCount = 0
            var reusedCount = 0
            var secureCount = 0
            var allVeryStrong = true
            var allAtLeastStrong = true

            for (credential in credentials) {
                val entropy = if (credential.entropyBits > 0) {
                    credential.entropyBits
                } else {
                    PasswordGenerator.calculateEntropy(credential.password)
                }
                val level = PasswordGenerator.evaluateStrength(entropy)
                val isReused = credential.password.isNotEmpty() &&
                    (passwordCounts[credential.password] ?: 0) > 1
                if (isReused) reusedCount++
                if (level.score >= PasswordStrengthLevel.STRONG.score) {
                    strongCount++
                } else {
                    weakCount++
                    allAtLeastStrong = false
                }
                if (level != PasswordStrengthLevel.VERY_STRONG) allVeryStrong = false
                if (level.score >= PasswordStrengthLevel.STRONG.score && !isReused) secureCount++
            }

            val score = secureCount * 100 / credentials.size
            val grade = when {
                allVeryStrong && reusedCount == 0 -> PasswordStrengthLevel.VERY_STRONG.label
                allAtLeastStrong && reusedCount == 0 -> PasswordStrengthLevel.STRONG.label
                score >= 50 -> PasswordStrengthLevel.FAIR.label
                else -> PasswordStrengthLevel.WEAK.label
            }
            return SecurityReport(
                totalCount = credentials.size,
                strongCount = strongCount,
                weakCount = weakCount,
                reusedCount = reusedCount,
                securityScorePercentage = score,
                gradeLabel = grade
            )
        }
    }
}
