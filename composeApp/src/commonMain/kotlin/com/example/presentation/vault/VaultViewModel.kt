package com.example.presentation.vault

import passilius.composeapp.generated.resources.*
import com.example.core.localization.*

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
            is VaultUiIntent.ChangeMasterPassword ->
                changeMasterPassword(intent.current, intent.newPassword, intent.confirm)
            is VaultUiIntent.ClearPasswordChangeResult -> {
                _uiState.update {
                    it.copy(passwordChangeError = null, passwordChangeSucceeded = false)
                }
            }
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
                    it.copy(unlockError = UiMessage(Res.string.use_minimum, VaultKeyStore.MIN_PASSWORD_LENGTH))
                }
                return
            }
            password != confirm -> {
                _uiState.update { it.copy(unlockError = UiMessage(Res.string.passwords_do_not_match)) }
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
                        unlockError = UiMessage(Res.string.could_not_create_the_vault_key)
                    )
                }
            }
        }
    }

    private fun submitMasterPassword(password: String) {
        if (_uiState.value.isUnlocking) return
        if (password.isBlank()) {
            _uiState.update { it.copy(unlockError = UiMessage(Res.string.enter_the_master_password)) }
            return
        }
        _uiState.update { it.copy(isUnlocking = true, unlockError = null) }
        viewModelScope.launch {
            val opened = try {
                VaultKeyStore.unlock(password)
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isUnlocking = false,
                        unlockError = UiMessage(Res.string.could_not_unlock_the_vault)
                    )
                }
                return@launch
            }
            if (!opened) {
                _uiState.update {
                    it.copy(isUnlocking = false, unlockError = UiMessage(Res.string.incorrect_master_password))
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

    private fun changeMasterPassword(current: String, newPassword: String, confirm: String) {
        if (_uiState.value.isChangingPassword) return
        when {
            newPassword.length < VaultKeyStore.MIN_PASSWORD_LENGTH -> {
                _uiState.update {
                    it.copy(passwordChangeError = UiMessage(Res.string.use_minimum, VaultKeyStore.MIN_PASSWORD_LENGTH))
                }
                return
            }
            newPassword != confirm -> {
                _uiState.update { it.copy(passwordChangeError = UiMessage(Res.string.passwords_do_not_match)) }
                return
            }
            newPassword == current -> {
                _uiState.update { it.copy(passwordChangeError = UiMessage(Res.string.choose_a_different_password)) }
                return
            }
        }
        _uiState.update {
            it.copy(
                isChangingPassword = true,
                passwordChangeError = null,
                passwordChangeSucceeded = false
            )
        }
        viewModelScope.launch {
            val changed = try {
                VaultKeyStore.changePassword(current, newPassword)
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        passwordChangeError = UiMessage(Res.string.could_not_change_the_master_password)
                    )
                }
                return@launch
            }
            if (!changed) {
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        passwordChangeError = UiMessage(Res.string.incorrect_master_password)
                    )
                }
                return@launch
            }
            _uiState.update {
                it.copy(
                    isChangingPassword = false,
                    passwordChangeError = null,
                    passwordChangeSucceeded = true
                )
            }
        }
    }

    private fun unlockWithBiometric() {
        if (_uiState.value.isUnlocking) return
        if (!BiometricVaultKey.unwrapAfterAuth()) {
            _uiState.update { it.copy(unlockError = UiMessage(Res.string.biometric_unlock_failed_use_the_master_password)) }
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
