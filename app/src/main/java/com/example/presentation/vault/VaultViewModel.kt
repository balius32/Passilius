package com.example.presentation.vault

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.util.ClipboardHelper
import com.example.domain.model.Credential
import com.example.domain.usecase.DeleteCredentialUseCase
import com.example.domain.usecase.GetVaultCredentialsUseCase
import com.example.domain.usecase.SaveCredentialUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VaultViewModel(
    application: Application,
    private val getVaultCredentialsUseCase: GetVaultCredentialsUseCase,
    private val saveCredentialUseCase: SaveCredentialUseCase,
    private val deleteCredentialUseCase: DeleteCredentialUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(VaultUiState())
    val uiState: StateFlow<VaultUiState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<VaultUiSingleEvent>()
    val events = _eventChannel.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        loadCredentials()
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
                ClipboardHelper.copyToClipboard(
                    context = getApplication(),
                    label = "${intent.service} Password",
                    text = intent.password,
                    isSensitive = true
                )
                showToast("Password copied to clipboard")
            }
            is VaultUiIntent.CopyUsername -> {
                ClipboardHelper.copyToClipboard(
                    context = getApplication(),
                    label = "Username",
                    text = intent.username,
                    isSensitive = false
                )
                showToast("Username copied to clipboard")
            }
            is VaultUiIntent.OpenCreate -> {
                _uiState.update { it.copy(editingCredential = null, isBottomSheetOpen = true) }
            }
            is VaultUiIntent.OpenEdit -> {
                _uiState.update { it.copy(editingCredential = intent.credential, isBottomSheetOpen = true) }
            }
            is VaultUiIntent.CloseBottomSheet -> {
                _uiState.update { it.copy(isBottomSheetOpen = false, editingCredential = null) }
            }
            is VaultUiIntent.SaveCredential -> {
                viewModelScope.launch {
                    saveCredentialUseCase(intent.credential)
                    _uiState.update { it.copy(isBottomSheetOpen = false, editingCredential = null) }
                    showToast("${intent.credential.service} account encrypted & saved")
                }
            }
            is VaultUiIntent.DeleteCredential -> {
                viewModelScope.launch {
                    deleteCredentialUseCase(intent.id)
                    showToast("Credential removed from vault")
                }
            }
            is VaultUiIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    toggleFavoriteUseCase(intent.id, intent.isFavorite)
                }
            }
            is VaultUiIntent.ClearToast -> {
                _uiState.update { it.copy(toastMessage = null) }
            }
            is VaultUiIntent.LockVault -> {
                _uiState.update { it.copy(isLocked = true) }
            }
            is VaultUiIntent.UnlockVault -> {
                _uiState.update { it.copy(isLocked = false) }
            }
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

    private fun showToast(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
        viewModelScope.launch {
            _eventChannel.send(VaultUiSingleEvent.ShowToast(msg))
        }
    }
}
