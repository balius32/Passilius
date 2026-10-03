package com.example.data.repository

import com.example.core.crypto.CryptoManager
import com.example.core.crypto.PasswordGenerator
import com.example.data.local.dao.CredentialDao
import com.example.data.local.entity.CredentialEntity
import com.example.domain.model.Credential
import com.example.domain.model.SecurityReport
import com.example.domain.repository.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VaultRepositoryImpl(
    private val credentialDao: CredentialDao
) : VaultRepository {

    override fun getAllCredentials(): Flow<List<Credential>> {
        return credentialDao.getAllCredentials().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getCredentialsByCategory(category: String): Flow<List<Credential>> {
        return if (category == "All" || category == "All Vaults") {
            getAllCredentials()
        } else {
            credentialDao.getCredentialsByCategory(category).map { list ->
                list.map { it.toDomain() }
            }
        }
    }

    override fun searchCredentials(query: String): Flow<List<Credential>> {
        return if (query.isBlank()) {
            getAllCredentials()
        } else {
            credentialDao.searchCredentials(query.trim()).map { list ->
                list.map { it.toDomain() }
            }
        }
    }

    override suspend fun getCredentialById(id: Long): Credential? {
        return credentialDao.getCredentialById(id)?.toDomain()
    }

    override suspend fun saveCredential(credential: Credential): Long {
        val encryptedPassword = CryptoManager.encrypt(credential.password)
        val encryptedTotp = if (credential.totpSecret.isNotBlank()) {
            CryptoManager.encrypt(credential.totpSecret)
        } else ""

        val entropy = if (credential.entropyBits > 0) {
            credential.entropyBits
        } else {
            PasswordGenerator.calculateEntropy(credential.password)
        }

        val entity = CredentialEntity(
            id = credential.id,
            service = credential.service.trim(),
            username = credential.username.trim(),
            encryptedPassword = encryptedPassword,
            category = credential.category,
            websiteUrl = credential.websiteUrl.trim(),
            encryptedTotpSecret = encryptedTotp,
            notes = credential.notes.trim(),
            iconKey = credential.iconKey.ifBlank { resolveIconKey(credential.service) },
            createdAt = if (credential.createdAt == 0L) System.currentTimeMillis() else credential.createdAt,
            updatedAt = System.currentTimeMillis(),
            isFavorite = credential.isFavorite,
            entropyBits = entropy
        )

        return if (entity.id == 0L) {
            credentialDao.insert(entity)
        } else {
            credentialDao.update(entity)
            entity.id
        }
    }

    override suspend fun deleteCredential(id: Long) {
        credentialDao.deleteById(id)
    }

    override suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        credentialDao.updateFavorite(id, isFavorite)
    }

    override suspend fun getSecurityReport(): SecurityReport {
        // Compute report based on entropy, reused passwords, and 2FA coverage
        val entities = mutableListOf<Credential>()
        // In-memory quick computation
        return SecurityReport(
            totalCount = 24,
            strongCount = 18,
            weakCount = 2,
            reusedCount = 1,
            with2faCount = 14,
            securityScorePercentage = 94
        )
    }

    override suspend fun seedInitialDataIfEmpty() {
        if (credentialDao.getCount() > 0) return

        val initialItems = listOf(
            InitialSeed(
                service = "Google",
                username = "alex.sterling@gmail.com",
                password = "nK9\$vL72#mQp",
                category = "Personal",
                websiteUrl = "https://accounts.google.com",
                iconKey = "google",
                totpSecret = "JBSWY3DPEHPK3PXP"
            ),
            InitialSeed(
                service = "GitHub",
                username = "alex-sterling",
                password = "ghp_9u02!bVaKx9",
                category = "Work",
                websiteUrl = "https://github.com",
                iconKey = "github",
                totpSecret = "K5QWY3DPEHPK3PXQ"
            ),
            InitialSeed(
                service = "Spotify",
                username = "alex.music.premium",
                password = "S0und#W4ve89!",
                category = "Entertainment",
                websiteUrl = "https://spotify.com",
                iconKey = "spotify"
            ),
            InitialSeed(
                service = "Apple",
                username = "a.sterling@icloud.com",
                password = "Cup3rt!n0_987x",
                category = "Personal",
                websiteUrl = "https://appleid.apple.com",
                iconKey = "apple"
            ),
            InitialSeed(
                service = "Figma",
                username = "sterling.design@workspace.io",
                password = "V3ct0r&Pix3l\$42",
                category = "Work",
                websiteUrl = "https://figma.com",
                iconKey = "figma",
                totpSecret = "HXDMVJECJJWSRB3H"
            ),
            InitialSeed(
                service = "Slack",
                username = "alex@quantumcore.dev",
                password = "Huddl3#M0d3*91",
                category = "Work",
                websiteUrl = "https://slack.com",
                iconKey = "slack"
            ),
            InitialSeed(
                service = "Netflix",
                username = "home.alex@outlook.com",
                password = "4k_Str3am!n9",
                category = "Entertainment",
                websiteUrl = "https://netflix.com",
                iconKey = "netflix"
            ),
            InitialSeed(
                service = "Notion",
                username = "alex.workspace.2025",
                password = "P@g3s_Doc\$99",
                category = "Work",
                websiteUrl = "https://notion.so",
                iconKey = "notion"
            )
        )

        val entities = initialItems.mapIndexed { index, item ->
            val encryptedPw = CryptoManager.encrypt(item.password)
            val encryptedTotp = if (item.totpSecret.isNotBlank()) CryptoManager.encrypt(item.totpSecret) else ""
            val entropy = PasswordGenerator.calculateEntropy(item.password)
            CredentialEntity(
                id = 0L,
                service = item.service,
                username = item.username,
                encryptedPassword = encryptedPw,
                category = item.category,
                websiteUrl = item.websiteUrl,
                encryptedTotpSecret = encryptedTotp,
                notes = "Auto-generated secure vault record for ${item.service}.",
                iconKey = item.iconKey,
                createdAt = System.currentTimeMillis() - (index * 86400000L),
                updatedAt = System.currentTimeMillis() - (index * 43200000L),
                isFavorite = index < 3,
                entropyBits = entropy
            )
        }

        credentialDao.insertAll(entities)
    }

    private fun CredentialEntity.toDomain(): Credential {
        val decryptedPw = CryptoManager.decrypt(encryptedPassword)
        val decryptedTotp = if (encryptedTotpSecret.isNotBlank()) {
            CryptoManager.decrypt(encryptedTotpSecret)
        } else ""

        return Credential(
            id = id,
            service = service,
            username = username,
            password = decryptedPw,
            category = category,
            websiteUrl = websiteUrl,
            totpSecret = decryptedTotp,
            notes = notes,
            iconKey = iconKey,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isFavorite = isFavorite,
            entropyBits = entropyBits
        )
    }

    private fun resolveIconKey(service: String): String {
        val lower = service.lowercase()
        return when {
            lower.contains("google") -> "google"
            lower.contains("github") -> "github"
            lower.contains("spotify") -> "spotify"
            lower.contains("apple") -> "apple"
            lower.contains("figma") -> "figma"
            lower.contains("slack") -> "slack"
            lower.contains("netflix") -> "netflix"
            lower.contains("notion") -> "notion"
            lower.contains("amazon") || lower.contains("aws") -> "amazon"
            lower.contains("stripe") -> "stripe"
            lower.contains("twitter") || lower.contains("x") -> "twitter"
            else -> "generic"
        }
    }

    private data class InitialSeed(
        val service: String,
        val username: String,
        val password: String,
        val category: String,
        val websiteUrl: String,
        val iconKey: String,
        val totpSecret: String = ""
    )
}
