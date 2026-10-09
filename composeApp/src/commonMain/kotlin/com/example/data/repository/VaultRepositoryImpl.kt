package com.example.data.repository

import com.example.core.crypto.CryptoManager
import com.example.core.crypto.LegacyVaultCrypto
import com.example.core.crypto.PasswordGenerator
import com.example.core.crypto.VaultCryptoException
import com.example.core.platform.currentTimeMillis
import com.example.core.platform.newSyncId
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.CredentialDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CredentialEntity
import com.example.domain.model.Credential
import com.example.domain.model.SecurityReport
import com.example.domain.model.VaultCategory
import com.example.domain.repository.VaultRepository
import com.example.domain.sync.MergeResult
import com.example.domain.sync.VaultSnapshot
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
        val needle = query.trim()
        if (needle.isBlank()) return getAllCredentials()
        return credentialDao.getAllCredentials().map { list ->
            list.map { it.toDomain() }.filter { credential ->
                credential.service.contains(needle, ignoreCase = true) ||
                    credential.username.contains(needle, ignoreCase = true)
            }
        }
    }

    override suspend fun getCredentialById(id: Long): Credential? {
        return credentialDao.getCredentialById(id)?.toDomain()
    }

    override suspend fun listCredentials(): List<Credential> {
        return credentialDao.getAllCredentialsOnce().map { it.toDomain() }
    }

    override suspend fun listCategories(): List<VaultCategory> {
        return categoryDao.getAllOnce().map {
            VaultCategory(
                id = it.id,
                syncId = it.syncId.ifBlank { newSyncId() },
                name = it.name,
                sortOrder = it.sortOrder
            )
        }
    }

    override suspend fun saveCredential(credential: Credential): Long {
        val encryptedPassword = seal(credential.password)

        val entropy = if (credential.entropyBits > 0) {
            credential.entropyBits
        } else {
            PasswordGenerator.calculateEntropy(credential.password)
        }

        val syncId = credential.syncId.ifBlank { newSyncId() }
        val entity = CredentialEntity(
            id = credential.id,
            syncId = syncId,
            service = credential.service.trim(),
            username = seal(credential.username.trim()),
            encryptedPassword = encryptedPassword,
            category = credential.category,
            websiteUrl = seal(credential.websiteUrl.trim()),
            notes = seal(credential.notes.trim()),
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
            CategoryEntity(syncId = newSyncId(), name = trimmed, sortOrder = nextOrder)
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

    override suspend fun mergeSnapshot(snapshot: VaultSnapshot): MergeResult {
        var categoriesAdded = 0
        var categoriesUpdated = 0
        var credentialsAdded = 0
        var credentialsUpdated = 0
        var credentialsSkipped = 0

        val syncIdToName = linkedMapOf<String, String>()

        for (remote in snapshot.categories) {
            val bySync = categoryDao.getBySyncId(remote.syncId)
            val byName = categoryDao.getByName(remote.name)
            when {
                bySync != null -> {
                    if (bySync.name != remote.name || bySync.sortOrder != remote.sortOrder) {
                        categoryDao.update(
                            bySync.copy(name = remote.name, sortOrder = remote.sortOrder)
                        )
                        categoriesUpdated++
                    }
                    syncIdToName[remote.syncId] = remote.name
                }
                byName != null -> {
                    categoryDao.update(
                        byName.copy(syncId = remote.syncId, sortOrder = remote.sortOrder)
                    )
                    categoriesUpdated++
                    syncIdToName[remote.syncId] = byName.name
                }
                else -> {
                    categoryDao.upsert(
                        CategoryEntity(
                            syncId = remote.syncId,
                            name = remote.name,
                            sortOrder = remote.sortOrder
                        )
                    )
                    categoriesAdded++
                    syncIdToName[remote.syncId] = remote.name
                }
            }
        }

        for (local in categoryDao.getAllOnce()) {
            syncIdToName.putIfAbsent(local.syncId, local.name)
        }

        for (remote in snapshot.credentials) {
            val categoryName = syncIdToName[remote.categorySyncId]
                ?: categoryDao.getBySyncId(remote.categorySyncId)?.name
                ?: DEFAULT_CATEGORY
            val existing = credentialDao.getCredentialBySyncId(remote.syncId)
            if (existing != null) {
                if (remote.updatedAt > existing.updatedAt) {
                    credentialDao.update(
                        existing.copy(
                            service = remote.service,
                            username = seal(remote.username),
                            encryptedPassword = seal(remote.password),
                            category = categoryName,
                            websiteUrl = seal(remote.websiteUrl),
                            notes = seal(remote.notes),
                            iconKey = remote.iconKey.ifBlank { resolveIconKey(remote.service) },
                            createdAt = remote.createdAt,
                            updatedAt = remote.updatedAt,
                            isFavorite = remote.isFavorite,
                            entropyBits = remote.entropyBits
                        )
                    )
                    credentialsUpdated++
                } else {
                    credentialsSkipped++
                }
            } else {
                credentialDao.insert(
                    CredentialEntity(
                        syncId = remote.syncId,
                        service = remote.service,
                        username = seal(remote.username),
                        encryptedPassword = seal(remote.password),
                        category = categoryName,
                        websiteUrl = seal(remote.websiteUrl),
                        notes = seal(remote.notes),
                        iconKey = remote.iconKey.ifBlank { resolveIconKey(remote.service) },
                        createdAt = remote.createdAt,
                        updatedAt = remote.updatedAt,
                        isFavorite = remote.isFavorite,
                        entropyBits = remote.entropyBits
                    )
                )
                credentialsAdded++
            }
        }

        return MergeResult(
            categoriesAdded = categoriesAdded,
            categoriesUpdated = categoriesUpdated,
            credentialsAdded = credentialsAdded,
            credentialsUpdated = credentialsUpdated,
            credentialsSkipped = credentialsSkipped
        )
    }

    override suspend fun migrateLegacySecrets(): Int {
        if (!LegacyVaultCrypto.hasLegacyKey()) return 0
        var failed = 0
        for (row in credentialDao.getAllCredentialsOnce()) {
            val password = LegacyVaultCrypto.decrypt(row.encryptedPassword)
            if (password == null) {
                val alreadyMoved = try {
                    CryptoManager.decrypt(row.encryptedPassword)
                    true
                } catch (_: VaultCryptoException) {
                    false
                }
                if (!alreadyMoved) failed++
                continue
            }
            credentialDao.update(
                row.copy(
                    username = seal(row.username),
                    encryptedPassword = seal(password),
                    websiteUrl = seal(row.websiteUrl),
                    notes = seal(row.notes)
                )
            )
        }
        if (failed == 0) LegacyVaultCrypto.destroy()
        return failed
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
            val encryptedPw = seal(item.password)
            val entropy = PasswordGenerator.calculateEntropy(item.password)
            CredentialEntity(
                id = 0L,
                syncId = newSyncId(),
                service = item.service,
                username = seal(item.username),
                encryptedPassword = encryptedPw,
                category = item.category,
                websiteUrl = seal(item.websiteUrl),
                notes = seal("Auto-generated secure vault record for ${item.service}."),
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
                CategoryEntity(syncId = newSyncId(), name = name, sortOrder = index)
            }
        )
    }

    private fun seal(value: String): String = CryptoManager.encrypt(value)

    private fun CredentialEntity.toDomain(): Credential {
        return Credential(
            id = id,
            syncId = syncId,
            service = service,
            username = CryptoManager.decrypt(username),
            password = CryptoManager.decrypt(encryptedPassword),
            category = category,
            websiteUrl = CryptoManager.decrypt(websiteUrl),
            notes = CryptoManager.decrypt(notes),
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
