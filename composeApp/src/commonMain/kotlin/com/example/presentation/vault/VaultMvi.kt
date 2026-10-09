package com.example.presentation.vault

import com.example.domain.model.Credential

data class VaultUiState(
    val credentials: List<Credential> = emptyList(),
    val categories: List<String> = listOf("All Vaults"),
    val selectableCategories: List<String> = emptyList(),
    val selectedCategory: String = "All Vaults",
    val searchQuery: String = "",
    val isSearchVisible: Boolean = false,
    val isLoading: Boolean = false,
    val editingCredential: Credential? = null,
    val isBottomSheetOpen: Boolean = false,
    val isCategorySheetOpen: Boolean = false,
    /** Bumped on every open so Compose remounts a fresh ModalBottomSheet. */
    val bottomSheetSessionId: Long = 0L,
    val categorySheetSessionId: Long = 0L,
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
    object OpenCreateCategory : VaultUiIntent
    object CloseCategorySheet : VaultUiIntent
    data class CreateCategory(val name: String) : VaultUiIntent
    object LockVault : VaultUiIntent
    object UnlockVault : VaultUiIntent
}
