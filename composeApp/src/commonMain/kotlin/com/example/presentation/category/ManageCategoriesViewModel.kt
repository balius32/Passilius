package com.example.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.VaultRepositoryImpl
import com.example.domain.usecase.AddCategoryUseCase
import com.example.domain.usecase.DeleteCategoryUseCase
import com.example.domain.usecase.ObserveCategoriesUseCase
import com.example.domain.usecase.RenameCategoryUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ManageCategoriesUiState(
    val categories: List<String> = emptyList(),
    val isSheetOpen: Boolean = false,
    val editingCategory: String? = null,
    val sheetSessionId: Long = 0L
)

sealed interface ManageCategoriesUiIntent {
    object OpenCreate : ManageCategoriesUiIntent
    data class OpenRename(val name: String) : ManageCategoriesUiIntent
    object CloseSheet : ManageCategoriesUiIntent
    data class SaveCategory(val name: String) : ManageCategoriesUiIntent
    data class DeleteCategory(val name: String) : ManageCategoriesUiIntent
}

class ManageCategoriesViewModel(
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val renameCategoryUseCase: RenameCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageCategoriesUiState())
    val uiState: StateFlow<ManageCategoriesUiState> = _uiState.asStateFlow()

    private var sheetJob: Job? = null

    init {
        viewModelScope.launch {
            observeCategoriesUseCase().collect { names ->
                _uiState.update { it.copy(categories = names) }
            }
        }
    }

    fun handleIntent(intent: ManageCategoriesUiIntent) {
        when (intent) {
            is ManageCategoriesUiIntent.OpenCreate -> openSheet(editing = null)
            is ManageCategoriesUiIntent.OpenRename -> openSheet(editing = intent.name)
            is ManageCategoriesUiIntent.CloseSheet -> closeSheet()
            is ManageCategoriesUiIntent.SaveCategory -> {
                viewModelScope.launch {
                    val editing = _uiState.value.editingCategory
                    val ok = if (editing == null) {
                        addCategoryUseCase(intent.name)
                    } else {
                        renameCategoryUseCase(editing, intent.name)
                    }
                    if (ok) closeSheet()
                }
            }
            is ManageCategoriesUiIntent.DeleteCategory -> {
                viewModelScope.launch {
                    deleteCategoryUseCase(intent.name)
                }
            }
        }
    }

    fun canDelete(name: String): Boolean =
        !name.equals(VaultRepositoryImpl.DEFAULT_CATEGORY, ignoreCase = true)

    fun canRename(name: String): Boolean =
        !name.equals(VaultRepositoryImpl.DEFAULT_CATEGORY, ignoreCase = true)

    private fun closeSheet() {
        sheetJob?.cancel()
        _uiState.update { it.copy(isSheetOpen = false, editingCategory = null) }
    }

    private fun openSheet(editing: String?) {
        sheetJob?.cancel()
        sheetJob = viewModelScope.launch {
            _uiState.update { it.copy(isSheetOpen = false, editingCategory = null) }
            delay(64)
            _uiState.update {
                it.copy(
                    isSheetOpen = true,
                    editingCategory = editing,
                    sheetSessionId = it.sheetSessionId + 1
                )
            }
        }
    }
}
