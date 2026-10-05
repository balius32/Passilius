package com.example.presentation.generator

import com.example.core.crypto.GeneratorConfig
import com.example.core.crypto.PasswordStrengthLevel

data class GeneratorUiState(
    val config: GeneratorConfig = GeneratorConfig(
        length = 16,
        includeUppercase = true,
        includeLowercase = true,
        includeNumbers = true,
        includeSymbols = true
    ),
    val generatedPassword: String = "",
    val entropyBits: Int = 104,
    val strengthLevel: PasswordStrengthLevel = PasswordStrengthLevel.VERY_STRONG,
    val isCopied: Boolean = false
)

sealed interface GeneratorUiIntent {
    data class UpdateLength(val length: Int) : GeneratorUiIntent
    data class ToggleUppercase(val enabled: Boolean) : GeneratorUiIntent
    data class ToggleLowercase(val enabled: Boolean) : GeneratorUiIntent
    data class ToggleNumbers(val enabled: Boolean) : GeneratorUiIntent
    data class ToggleSymbols(val enabled: Boolean) : GeneratorUiIntent
    object Regenerate : GeneratorUiIntent
    object CopyPassword : GeneratorUiIntent
}
