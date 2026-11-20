package com.example.core.localization

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLanguage { SYSTEM, ENGLISH, PERSIAN;
    companion object {
        fun fromStored(value: String?): AppLanguage = entries.firstOrNull { it.name == value } ?: SYSTEM
    }
}

fun resolveLanguage(selection: AppLanguage, deviceLanguage: String): String = when (selection) {
    AppLanguage.ENGLISH -> "en"
    AppLanguage.PERSIAN -> "fa"
    AppLanguage.SYSTEM -> if (deviceLanguage.substringBefore('-').substringBefore('_').equals("fa", true)) "fa" else "en"
}

expect object LanguagePreferences {
    fun read(): String?
    fun write(value: String)
    fun deviceLanguage(): String
    fun applyLocale(language: String)
}

object LanguageController {
    var selection by mutableStateOf(AppLanguage.SYSTEM)
        private set
    var language by mutableStateOf("en")
        private set

    fun initialize() {
        selection = AppLanguage.fromStored(LanguagePreferences.read())
        refreshSystemLanguage()
    }

    fun select(value: AppLanguage) {
        LanguagePreferences.write(value.name)
        selection = value
        refreshSystemLanguage()
    }

    fun refreshSystemLanguage() {
        language = resolveLanguage(selection, LanguagePreferences.deviceLanguage())
        LanguagePreferences.applyLocale(language)
    }
}
