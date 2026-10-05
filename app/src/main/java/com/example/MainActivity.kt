package com.example

import android.os.Bundle
import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.core.designsystem.VaultTheme
import com.example.data.local.database.VaultDatabase
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
import com.example.presentation.navigation.VaultApp
import com.example.presentation.vault.VaultViewModel
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    private lateinit var vaultViewModel: VaultViewModel
    private lateinit var generatorViewModel: GeneratorViewModel
    private lateinit var manageCategoriesViewModel: ManageCategoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        // Initialize Clean Architecture & Data layers
        val database = VaultDatabase.getInstance(applicationContext)
        val repository = VaultRepositoryImpl(
            credentialDao = database.credentialDao(),
            categoryDao = database.categoryDao()
        )

        // Seed initial sample data asynchronously if needed
        lifecycleScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        val getCredentialsUseCase = GetVaultCredentialsUseCase(repository)
        val saveCredentialUseCase = SaveCredentialUseCase(repository)
        val deleteCredentialUseCase = DeleteCredentialUseCase(repository)
        val toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
        val observeCategoriesUseCase = ObserveCategoriesUseCase(repository)
        val addCategoryUseCase = AddCategoryUseCase(repository)
        val renameCategoryUseCase = RenameCategoryUseCase(repository)
        val deleteCategoryUseCase = DeleteCategoryUseCase(repository)
        val generatePasswordUseCase = GeneratePasswordUseCase()

        vaultViewModel = VaultViewModel(
            application = application,
            getVaultCredentialsUseCase = getCredentialsUseCase,
            saveCredentialUseCase = saveCredentialUseCase,
            deleteCredentialUseCase = deleteCredentialUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
            observeCategoriesUseCase = observeCategoriesUseCase,
            addCategoryUseCase = addCategoryUseCase
        )

        manageCategoriesViewModel = ManageCategoriesViewModel(
            observeCategoriesUseCase = observeCategoriesUseCase,
            addCategoryUseCase = addCategoryUseCase,
            renameCategoryUseCase = renameCategoryUseCase,
            deleteCategoryUseCase = deleteCategoryUseCase
        )

        generatorViewModel = GeneratorViewModel(
            application = application,
            generatePasswordUseCase = generatePasswordUseCase
        )

        setContent {
            VaultTheme {
                VaultApp(
                    vaultViewModel = vaultViewModel,
                    generatorViewModel = generatorViewModel,
                    manageCategoriesViewModel = manageCategoriesViewModel
                )
            }
        }
    }
}
