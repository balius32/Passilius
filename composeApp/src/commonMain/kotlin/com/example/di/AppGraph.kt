package com.example.di

import com.example.core.sync.SyncClient
import com.example.core.sync.SyncHost
import com.example.data.local.database.createVaultDatabase
import com.example.data.repository.VaultRepositoryImpl
import com.example.domain.usecase.AddCategoryUseCase
import com.example.domain.usecase.BuildVaultSnapshotUseCase
import com.example.domain.usecase.DeleteCategoryUseCase
import com.example.domain.usecase.DeleteCredentialUseCase
import com.example.domain.usecase.GeneratePasswordUseCase
import com.example.domain.usecase.GetVaultCredentialsUseCase
import com.example.domain.usecase.MergeVaultSnapshotUseCase
import com.example.domain.usecase.ObserveCategoriesUseCase
import com.example.domain.usecase.RenameCategoryUseCase
import com.example.domain.usecase.SaveCredentialUseCase
import com.example.presentation.category.ManageCategoriesViewModel
import com.example.presentation.generator.GeneratorViewModel
import com.example.presentation.sync.SyncViewModel
import com.example.presentation.vault.VaultViewModel

class AppGraph private constructor() {
    private val database = createVaultDatabase()
    private val repository = VaultRepositoryImpl(
        credentialDao = database.credentialDao(),
        categoryDao = database.categoryDao()
    )

    private val getCredentialsUseCase = GetVaultCredentialsUseCase(repository)
    private val saveCredentialUseCase = SaveCredentialUseCase(repository)
    private val deleteCredentialUseCase = DeleteCredentialUseCase(repository)
    private val observeCategoriesUseCase = ObserveCategoriesUseCase(repository)
    private val addCategoryUseCase = AddCategoryUseCase(repository)
    private val renameCategoryUseCase = RenameCategoryUseCase(repository)
    private val deleteCategoryUseCase = DeleteCategoryUseCase(repository)
    private val generatePasswordUseCase = GeneratePasswordUseCase()
    private val buildVaultSnapshotUseCase = BuildVaultSnapshotUseCase(repository)
    private val mergeVaultSnapshotUseCase = MergeVaultSnapshotUseCase(repository)

    val vaultViewModel = VaultViewModel(
        getVaultCredentialsUseCase = getCredentialsUseCase,
        saveCredentialUseCase = saveCredentialUseCase,
        deleteCredentialUseCase = deleteCredentialUseCase,
        observeCategoriesUseCase = observeCategoriesUseCase,
        addCategoryUseCase = addCategoryUseCase,
        onVaultOpened = {
            val failed = repository.migrateLegacySecrets()
            repository.seedInitialDataIfEmpty()
            failed
        }
    )

    val manageCategoriesViewModel = ManageCategoriesViewModel(
        observeCategoriesUseCase = observeCategoriesUseCase,
        addCategoryUseCase = addCategoryUseCase,
        renameCategoryUseCase = renameCategoryUseCase,
        deleteCategoryUseCase = deleteCategoryUseCase
    )

    val generatorViewModel = GeneratorViewModel(
        generatePasswordUseCase = generatePasswordUseCase
    )

    val syncViewModel = SyncViewModel(
        buildSnapshot = buildVaultSnapshotUseCase,
        mergeSnapshot = mergeVaultSnapshotUseCase,
        syncHost = SyncHost(),
        syncClient = SyncClient()
    )

    companion object {
        fun create(): AppGraph = AppGraph()
    }
}
