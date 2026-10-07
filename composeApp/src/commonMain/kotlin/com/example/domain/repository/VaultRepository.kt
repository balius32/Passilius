package com.example.domain.repository

import com.example.domain.model.Credential
import com.example.domain.model.SecurityReport
import kotlinx.coroutines.flow.Flow

interface VaultRepository {
    fun getAllCredentials(): Flow<List<Credential>>
    fun getCredentialsByCategory(category: String): Flow<List<Credential>>
    fun searchCredentials(query: String): Flow<List<Credential>>
    suspend fun getCredentialById(id: Long): Credential?
    suspend fun saveCredential(credential: Credential): Long
    suspend fun deleteCredential(id: Long)
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)
    suspend fun getSecurityReport(): SecurityReport
    suspend fun seedInitialDataIfEmpty()

    fun observeCategories(): Flow<List<String>>
    suspend fun addCategory(name: String): Boolean
    suspend fun renameCategory(oldName: String, newName: String): Boolean
    suspend fun deleteCategory(name: String): Boolean
}
