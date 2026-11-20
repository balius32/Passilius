package com.example.core.localization

import android.content.Context
import android.content.res.Resources
import java.util.Locale

actual object LanguagePreferences {
    private lateinit var context: Context
    fun bind(value: Context) { context = value.applicationContext }
    actual fun read(): String? = context.getSharedPreferences("passilius_language", Context.MODE_PRIVATE).getString("language", null)
    actual fun write(value: String) {
        context.getSharedPreferences("passilius_language", Context.MODE_PRIVATE).edit().putString("language", value).apply()
    }
    actual fun deviceLanguage(): String = Resources.getSystem().configuration.locales[0].language
    actual fun applyLocale(language: String) { Locale.setDefault(Locale.forLanguageTag(language)) }
}
