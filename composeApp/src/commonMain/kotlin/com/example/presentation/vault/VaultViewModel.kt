package com.example.presentation.vault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.crypto.BiometricVaultKey
import com.example.core.crypto.VaultKeyStore
import com.example.core.util.ClipboardHelper
import com.example.domain.model.Credential
import com.example.domain.usecase.AddCategoryUseCase
import com.example.domain.usecase.DeleteCredentialUseCase
import com.example.domain.usecase.GetVaultCredentialsUseCase
import com.example.domain.usecase.ObserveCategoriesUseCase
import com.example.domain.usecase.SaveCredentialUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VaultViewModel(
    private val getVaultCredentialsUseCase: GetVaultCredentialsUseCase,
    private val saveCredentialUseCase: SaveCredentialUseCase,
    private val deleteCredentialUseCase: DeleteCredentialUseCase,
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val onVaultOpened: suspend () -> Int = { 0 }
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        VaultUiState(needsMasterSetup = !VaultKeyStore.isInitialized())
    )
    val uiState: StateFlow<VaultUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var sheetOpenJob: Job? = null
    private var categorySheetJob: Job? = null

    init {
        observeCategories()
    }

    fun handleIntent(intent: VaultUiIntent) {
        when (intent) {
            is VaultUiIntent.SelectCategory -> {
                _uiState.update { it.copy(selectedCategory = intent.category) }
                loadCredentials()
            }
            is VaultUiIntent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
                loadCredentials()
            }
            is VaultUiIntent.ToggleSearch -> {
                val newVisibility = !_uiState.value.isSearchVisible
                _uiState.update {
                    it.copy(
                        isSearchVisible = newVisibility,
                        searchQuery = if (!newVisibility) "" else it.searchQuery
                    )
                }
                loadCredentials()
            }
            is VaultUiIntent.CopyPassword -> {
                ClipboardHelper.copy(text = intent.password, isSensitive = true)
            }
            is VaultUiIntent.CopyUsername -> {
                ClipboardHelper.copy(text = intent.username, isSensitive = true)
            }
            is VaultUiIntent.OpenCreate -> {
                openBottomSheet(editingCredential = null)
            }
            is VaultUiIntent.OpenEdit -> {
                openBottomSheet(editingCredential = intent.credential)
            }
            is VaultUiIntent.CloseBottomSheet -> {
                closeBottomSheet()
            }
            is VaultUiIntent.SaveCredential -> {
                viewModelScope.launch {
                    saveCredentialUseCase(intent.credential)
                    closeBottomSheet()
                }
            }
            is VaultUiIntent.DeleteCredential -> {
                viewModelScope.launch {
                    deleteCredentialUseCase(intent.id)
                }
            }
            is VaultUiIntent.OpenCreateCategory -> {
                openCategorySheet()
            }
            is VaultUiIntent.CloseCategorySheet -> {
                closeCategorySheet()
            }
            is VaultUiIntent.CreateCategory -> {
                viewModelScope.launch {
                    val ok = addCategoryUseCase(intent.name)
                    if (ok) {
                        closeCategorySheet()
                    }
                }
            }
            is VaultUiIntent.LockVault -> lock()
            is VaultUiIntent.CreateMasterPassword -> createMasterPassword(intent.password, intent.confirm)
            is VaultUiIntent.SubmitMasterPassword -> submitMasterPassword(intent.password)
            is VaultUiIntent.UnlockWithBiometric -> unlockWithBiometric()
        }
    }

    private fun observeCategories() {
        viewModelScope.launch {
            observeCategoriesUseCase().collect { names ->
                val selected = _uiState.value.selectedCategory
                val stillValid = selected == "All Vaults" ||
                    names.any { it.equals(selected, ignoreCase = true) }
                _uiState.update {
                    it.copy(
                        selectableCategories = names,
                        categories = listOf("All Vaults") + names,
                        selectedCategory = if (stillValid) selected else "All Vaults"
                    )
                }
                if (!stillValid) {
                    loadCredentials()
                }
            }
        }
    }

    private fun closeBottomSheet() {
        sheetOpenJob?.cancel()
        _uiState.update {
            it.copy(isBottomSheetOpen = false, editingCredential = null)
        }
    }

    private fun openBottomSheet(editingCredential: Credential?) {
        sheetOpenJob?.cancel()
        sheetOpenJob = viewModelScope.launch {
            _uiState.update {
                it.copy(isBottomSheetOpen = false, editingCredential = null)
            }
            delay(64)
            _uiState.update {
                it.copy(
                    editingCredential = editingCredential,
                    isBottomSheetOpen = true,
                    bottomSheetSessionId = it.bottomSheetSessionId + 1
                )
            }
        }
    }

    private fun closeCategorySheet() {
        categorySheetJob?.cancel()
        _uiState.update { it.copy(isCategorySheetOpen = false) }
    }

    private fun openCategorySheet() {
        categorySheetJob?.cancel()
        categorySheetJob = viewModelScope.launch {
            _uiState.update { it.copy(isCategorySheetOpen = false) }
            delay(64)
            _uiState.update {
                it.copy(
                    isCategorySheetOpen = true,
                    categorySheetSessionId = it.categorySheetSessionId + 1
                )
            }
        }
    }

    private fun lock() {
        VaultKeyStore.lock()
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                isLocked = true,
                credentials = emptyList(),
                editingCredential = null,
                isBottomSheetOpen = false,
                isCategorySheetOpen = false,
                unlockError = null,
                isUnlocking = false
            )
        }
    }

    private fun createMasterPassword(password: String, confirm: String) {
        if (_uiState.value.isUnlocking) return
        when {
            password.length < VaultKeyStore.MIN_PASSWORD_LENGTH -> {
                _uiState.update {
                    it.copy(unlockError = "Use at least ${VaultKeyStore.MIN_PASSWORD_LENGTH} characters")
                }
                return
            }
            password != confirm -> {
                _uiState.update { it.copy(unlockError = "Passwords do not match") }
                return
            }
        }
        _uiState.update { it.copy(isUnlocking = true, unlockError = null) }
        viewModelScope.launch {
            try {
                VaultKeyStore.create(password)
                val failed = onVaultOpened()
                _uiState.update {
                    it.copy(
                        isLocked = false,
                        needsMasterSetup = false,
                        isUnlocking = false,
                        unlockError = null,
                        legacyFailures = failed
                    )
                }
                loadCredentials()
            } catch (error: Exception) {
                VaultKeyStore.lock()
                _uiState.update {
                    it.copy(
                        isUnlocking = false,
                        unlockError = error.message ?: "Could not create the vault key"
                    )
                }
            }
        }
    }

    private fun submitMasterPassword(password: String) {
        if (_uiState.value.isUnlocking) return
        _uiState.update { it.copy(isUnlocking = true, unlockError = null) }
        viewModelScope.launch {
            val opened = VaultKeyStore.unlock(password)
            if (!opened) {
                _uiState.update {
                    it.copy(isUnlocking = false, unlockError = "Incorrect master password")
                }
                return@launch
            }
            val failed = onVaultOpened()
            _uiState.update {
                it.copy(
                    isLocked = false,
                    isUnlocking = false,
                    unlockError = null,
                    legacyFailures = failed
                )
            }
            loadCredentials()
        }
    }

    private fun unlockWithBiometric() {
        if (_uiState.value.isUnlocking) return
        if (!BiometricVaultKey.unwrapAfterAuth()) {
            _uiState.update { it.copy(unlockError = "Biometric unlock failed. Use the master password.") }
            return
        }
        _uiState.update { it.copy(isUnlocking = true, unlockError = null) }
        viewModelScope.launch {
            val failed = onVaultOpened()
            _uiState.update {
                it.copy(
                    isLocked = false,
                    isUnlocking = false,
                    unlockError = null,
                    legacyFailures = failed
                )
            }
            loadCredentials()
        }
    }

    private fun loadCredentials() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val state = _uiState.value
            getVaultCredentialsUseCase(
                category = state.selectedCategory,
                query = state.searchQuery
            ).collect { list ->
                _uiState.update { it.copy(credentials = list, isLoading = false) }
            }
        }
    }
}
