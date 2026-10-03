package com.example.presentation.vault

import com.example.domain.model.Credential

data class VaultUiState(
    val credentials: List<Credential> = emptyList(),
    val categories: List<String> = listOf("All Vaults", "Personal", "Work", "Finance", "Entertainment"),
    val selectedCategory: String = "All Vaults",
    val searchQuery: String = "",
    val isSearchVisible: Boolean = false,
    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val editingCredential: Credential? = null,
    val isBottomSheetOpen: Boolean = false,
    val isLocked: Boolean = false,
    val masterPin: String = "1234"
)

sealed interface VaultUiIntent {
    data class SelectCategory(val category: String) : VaultUiIntent
    data class UpdateSearchQuery(val query: String) : VaultUiIntent
    object ToggleSearch : VaultUiIntent
    data class CopyPassword(val password: String, val service: String) : VaultUiIntent
    data class CopyUsername(val username: String) : VaultUiIntent
    data class OpenEdit(val credential: Credential) : VaultUiIntent
    object OpenCreate : VaultUiIntent
    object CloseBottomSheet : VaultUiIntent
    data class SaveCredential(val credential: Credential) : VaultUiIntent
    data class DeleteCredential(val id: Long) : VaultUiIntent
    data class ToggleFavorite(val id: Long, val isFavorite: Boolean) : VaultUiIntent
    object ClearToast : VaultUiIntent
    object LockVault : VaultUiIntent
    object UnlockVault : VaultUiIntent
}

sealed interface VaultUiSingleEvent {
    data class ShowToast(val message: String) : VaultUiSingleEvent
}
