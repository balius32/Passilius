package com.example.presentation.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.util.ClipboardHelper
import com.example.domain.usecase.GeneratePasswordUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GeneratorViewModel(
    private val generatePasswordUseCase: GeneratePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GeneratorUiState())
    val uiState: StateFlow<GeneratorUiState> = _uiState.asStateFlow()

    private var copiedResetJob: Job? = null

    init {
        generate()
    }

    fun handleIntent(intent: GeneratorUiIntent) {
        when (intent) {
            is GeneratorUiIntent.UpdateLength -> {
                _uiState.update { it.copy(config = it.config.copy(length = intent.length)) }
            }
            is GeneratorUiIntent.ToggleUppercase -> {
                val newConfig = _uiState.value.config.copy(includeUppercase = intent.enabled)
                if (isValidConfig(newConfig)) {
                    _uiState.update { it.copy(config = newConfig) }
                    generate()
                }
            }
            is GeneratorUiIntent.ToggleLowercase -> {
                val newConfig = _uiState.value.config.copy(includeLowercase = intent.enabled)
                if (isValidConfig(newConfig)) {
                    _uiState.update { it.copy(config = newConfig) }
                    generate()
                }
            }
            is GeneratorUiIntent.ToggleNumbers -> {
                val newConfig = _uiState.value.config.copy(includeNumbers = intent.enabled)
                if (isValidConfig(newConfig)) {
                    _uiState.update { it.copy(config = newConfig) }
                    generate()
                }
            }
            is GeneratorUiIntent.ToggleSymbols -> {
                val newConfig = _uiState.value.config.copy(includeSymbols = intent.enabled)
                if (isValidConfig(newConfig)) {
                    _uiState.update { it.copy(config = newConfig) }
                    generate()
                }
            }
            is GeneratorUiIntent.Regenerate -> {
                generate()
            }
            is GeneratorUiIntent.CopyPassword -> {
                val pw = _uiState.value.generatedPassword
                ClipboardHelper.copy(
                    label = "Generated Password",
                    text = pw,
                    isSensitive = true
                )
                _uiState.update { it.copy(isCopied = true) }
                copiedResetJob?.cancel()
                copiedResetJob = viewModelScope.launch {
                    delay(2000)
                    _uiState.update { it.copy(isCopied = false) }
                }
            }
        }
    }

    private fun generate() {
        val result = generatePasswordUseCase(_uiState.value.config)
        _uiState.update {
            it.copy(
                generatedPassword = result.password,
                entropyBits = result.entropyBits,
                strengthLevel = result.strengthLevel,
                isCopied = false
            )
        }
    }

    private fun isValidConfig(config: com.example.core.crypto.GeneratorConfig): Boolean {
        return config.includeUppercase || config.includeLowercase ||
            config.includeNumbers || config.includeSymbols
    }
}
