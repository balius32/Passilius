package com.example.domain.usecase

import com.example.core.crypto.GeneratedSecret
import com.example.core.crypto.GeneratorConfig
import com.example.core.crypto.PasswordGenerator
import com.example.domain.model.Credential
import com.example.domain.model.SecurityReport
import com.example.domain.repository.VaultRepository
import kotlinx.coroutines.flow.Flow

class GetVaultCredentialsUseCase(
    private val repository: VaultRepository
) {
    operator fun invoke(category: String = "All Vaults", query: String = ""): Flow<List<Credential>> {
        return if (query.isNotBlank()) {
            repository.searchCredentials(query)
        } else {
            repository.getCredentialsByCategory(category)
        }
    }
}

class SaveCredentialUseCase(
    private val repository: VaultRepository
) {
    suspend operator fun invoke(credential: Credential): Long {
        return repository.saveCredential(credential)
    }
}

class DeleteCredentialUseCase(
    private val repository: VaultRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteCredential(id)
    }
}

class ToggleFavoriteUseCase(
    private val repository: VaultRepository
) {
    suspend operator fun invoke(id: Long, isFavorite: Boolean) {
        repository.toggleFavorite(id, isFavorite)
    }
}

class GeneratePasswordUseCase {
    operator fun invoke(config: GeneratorConfig): GeneratedSecret {
        return PasswordGenerator.generate(config)
    }
}

class ObserveCategoriesUseCase(
    private val repository: VaultRepository
) {
    operator fun invoke(): Flow<List<String>> = repository.observeCategories()
}

class AddCategoryUseCase(
    private val repository: VaultRepository
) {
    suspend operator fun invoke(name: String): Boolean = repository.addCategory(name)
}

class RenameCategoryUseCase(
    private val repository: VaultRepository
) {
    suspend operator fun invoke(oldName: String, newName: String): Boolean =
        repository.renameCategory(oldName, newName)
}

class DeleteCategoryUseCase(
    private val repository: VaultRepository
) {
    suspend operator fun invoke(name: String): Boolean = repository.deleteCategory(name)
}
