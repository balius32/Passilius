package com.example.di

import com.example.data.local.database.createVaultDatabase
import com.example.data.repository.VaultRepositoryImpl
import com.example.domain.usecase.AddCategoryUseCase
import com.example.domain.usecase.DeleteCategoryUseCase
import com.example.domain.usecase.DeleteCredentialUseCase
import com.example.domain.usecase.GeneratePasswordUseCase
import com.example.domain.usecase.GetVaultCredentialsUseCase
import com.example.domain.usecase.ObserveCategoriesUseCase
import com.example.domain.usecase.RenameCategoryUseCase
import com.example.domain.usecase.SaveCredentialUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import com.example.presentation.category.ManageCategoriesViewModel
import com.example.presentation.generator.GeneratorViewModel
import com.example.presentation.vault.VaultViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppGraph private constructor() {
    private val database = createVaultDatabase()
    private val repository = VaultRepositoryImpl(
        credentialDao = database.credentialDao(),
        categoryDao = database.categoryDao()
    )

    private val getCredentialsUseCase = GetVaultCredentialsUseCase(repository)
    private val saveCredentialUseCase = SaveCredentialUseCase(repository)
    private val deleteCredentialUseCase = DeleteCredentialUseCase(repository)
    private val toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
    private val observeCategoriesUseCase = ObserveCategoriesUseCase(repository)
    private val addCategoryUseCase = AddCategoryUseCase(repository)
    private val renameCategoryUseCase = RenameCategoryUseCase(repository)
    private val deleteCategoryUseCase = DeleteCategoryUseCase(repository)
    private val generatePasswordUseCase = GeneratePasswordUseCase()

    val vaultViewModel = VaultViewModel(
        getVaultCredentialsUseCase = getCredentialsUseCase,
        saveCredentialUseCase = saveCredentialUseCase,
        deleteCredentialUseCase = deleteCredentialUseCase,
        toggleFavoriteUseCase = toggleFavoriteUseCase,
        observeCategoriesUseCase = observeCategoriesUseCase,
        addCategoryUseCase = addCategoryUseCase
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

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    companion object {
        fun create(): AppGraph = AppGraph()
    }
}
