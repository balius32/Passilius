package com.example.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.SecurityReport
import com.example.domain.usecase.GetVaultCredentialsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    getVaultCredentialsUseCase: GetVaultCredentialsUseCase
) : ViewModel() {

    val securityReport: StateFlow<SecurityReport> = getVaultCredentialsUseCase()
        .map { SecurityReport.from(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SecurityReport()
        )
}
