package com.example.domain.repository

import com.example.domain.model.Credential
import com.example.domain.model.SecurityReport
import com.example.domain.model.VaultCategory
import com.example.domain.sync.MergeResult
import com.example.domain.sync.VaultSnapshot
import kotlinx.coroutines.flow.Flow

interface VaultRepository {
    fun getAllCredentials(): Flow<List<Credential>>
    fun getCredentialsByCategory(category: String): Flow<List<Credential>>
    fun searchCredentials(query: String): Flow<List<Credential>>
    suspend fun getCredentialById(id: Long): Credential?
    suspend fun listCredentials(): List<Credential>
    suspend fun listCategories(): List<VaultCategory>
    suspend fun saveCredential(credential: Credential): Long
    suspend fun deleteCredential(id: Long)
    suspend fun getSecurityReport(): SecurityReport
    suspend fun seedInitialDataIfEmpty()
    suspend fun mergeSnapshot(snapshot: VaultSnapshot): MergeResult

    fun observeCategories(): Flow<List<String>>
    suspend fun addCategory(name: String): Boolean
    suspend fun renameCategory(oldName: String, newName: String): Boolean
    suspend fun deleteCategory(name: String): Boolean
}
