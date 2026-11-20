package com.example.core.localization

import java.util.Locale
import java.util.prefs.Preferences

actual object LanguagePreferences {
    private val systemLanguage = Locale.getDefault().language
    private val preferences = Preferences.userRoot().node("com.example.passilius.language")
    actual fun read(): String? = preferences.get("language", null)
    actual fun write(value: String) { preferences.put("language", value); preferences.flush() }
    actual fun deviceLanguage(): String = systemLanguage
    actual fun applyLocale(language: String) { Locale.setDefault(Locale.forLanguageTag(language)) }
}
