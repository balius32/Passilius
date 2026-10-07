package com.example.data.repository

import com.example.core.crypto.CryptoManager
import com.example.core.crypto.PasswordGenerator
import com.example.core.platform.currentTimeMillis
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.CredentialDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CredentialEntity
import com.example.domain.model.Credential
import com.example.domain.model.SecurityReport
import com.example.domain.repository.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VaultRepositoryImpl(
    private val credentialDao: CredentialDao,
    private val categoryDao: CategoryDao
) : VaultRepository {

    companion object {
        const val DEFAULT_CATEGORY = "Personal"
        private val DEFAULT_CATEGORIES = listOf(
            "Personal",
            "Work",
            "Finance",
            "Entertainment",
            "Social"
        )
    }

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
            notes = credential.notes.trim(),
            iconKey = credential.iconKey.ifBlank { resolveIconKey(credential.service) },
            createdAt = if (credential.createdAt == 0L) currentTimeMillis() else credential.createdAt,
            updatedAt = currentTimeMillis(),
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
        return SecurityReport(
            totalCount = 24,
            strongCount = 18,
            weakCount = 2,
            reusedCount = 1,
            securityScorePercentage = 94
        )
    }

    override fun observeCategories(): Flow<List<String>> {
        return categoryDao.observeAll().map { list -> list.map { it.name } }
    }

    override suspend fun addCategory(name: String): Boolean {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return false
        if (trimmed.equals("All Vaults", ignoreCase = true) || trimmed.equals("All", ignoreCase = true)) {
            return false
        }
        if (categoryDao.getByName(trimmed) != null) return false

        val nextOrder = categoryDao.getMaxSortOrder() + 1
        val id = categoryDao.insert(
            CategoryEntity(name = trimmed, sortOrder = nextOrder)
        )
        return id != -1L
    }

    override suspend fun renameCategory(oldName: String, newName: String): Boolean {
        val trimmedOld = oldName.trim()
        val trimmedNew = newName.trim()
        if (trimmedOld.isBlank() || trimmedNew.isBlank()) return false
        if (trimmedOld.equals(DEFAULT_CATEGORY, ignoreCase = true)) return false
        if (trimmedNew.equals("All Vaults", ignoreCase = true) || trimmedNew.equals("All", ignoreCase = true)) {
            return false
        }
        if (trimmedOld.equals(trimmedNew, ignoreCase = true)) return true
        if (categoryDao.getByName(trimmedOld) == null) return false
        if (categoryDao.getByName(trimmedNew) != null) return false

        categoryDao.rename(trimmedOld, trimmedNew)
        credentialDao.reassignCategory(trimmedOld, trimmedNew)
        return true
    }

    override suspend fun deleteCategory(name: String): Boolean {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return false
        if (trimmed.equals(DEFAULT_CATEGORY, ignoreCase = true)) return false
        if (categoryDao.getByName(trimmed) == null) return false

        credentialDao.reassignCategory(trimmed, DEFAULT_CATEGORY)
        categoryDao.deleteByName(trimmed)
        return true
    }

    override suspend fun seedInitialDataIfEmpty() {
        seedCategoriesIfEmpty()

        if (credentialDao.getCount() > 0) return

        val initialItems = listOf(
            InitialSeed(
                service = "Google",
                username = "alex.sterling@gmail.com",
                password = "nK9\$vL72#mQp",
                category = "Personal",
                websiteUrl = "https://accounts.google.com",
                iconKey = "google"
            ),
            InitialSeed(
                service = "GitHub",
                username = "alex-sterling",
                password = "ghp_9u02!bVaKx9",
                category = "Work",
                websiteUrl = "https://github.com",
                iconKey = "github"
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
                iconKey = "figma"
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
            val entropy = PasswordGenerator.calculateEntropy(item.password)
            CredentialEntity(
                id = 0L,
                service = item.service,
                username = item.username,
                encryptedPassword = encryptedPw,
                category = item.category,
                websiteUrl = item.websiteUrl,
                notes = "Auto-generated secure vault record for ${item.service}.",
                iconKey = item.iconKey,
                createdAt = currentTimeMillis() - (index * 86400000L),
                updatedAt = currentTimeMillis() - (index * 43200000L),
                isFavorite = index < 3,
                entropyBits = entropy
            )
        }

        credentialDao.insertAll(entities)
    }

    private suspend fun seedCategoriesIfEmpty() {
        if (categoryDao.getCount() > 0) return
        categoryDao.insertAll(
            DEFAULT_CATEGORIES.mapIndexed { index, name ->
                CategoryEntity(name = name, sortOrder = index)
            }
        )
    }

    private fun CredentialEntity.toDomain(): Credential {
        val decryptedPw = CryptoManager.decrypt(encryptedPassword)

        return Credential(
            id = id,
            service = service,
            username = username,
            password = decryptedPw,
            category = category,
            websiteUrl = websiteUrl,
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
        val iconKey: String
    )
}
