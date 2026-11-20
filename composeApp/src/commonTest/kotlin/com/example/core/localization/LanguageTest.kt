package com.example.core.localization

import kotlin.test.Test
import kotlin.test.assertEquals

class LanguageTest {
    @Test fun invalidPreferencesUseSystem() {
        assertEquals(AppLanguage.SYSTEM, AppLanguage.fromStored(null))
        assertEquals(AppLanguage.SYSTEM, AppLanguage.fromStored("invalid"))
        AppLanguage.entries.forEach { assertEquals(it, AppLanguage.fromStored(it.name)) }
    }
    @Test fun systemResolutionSupportsPersianVariants() {
        listOf("fa", "fa-IR", "fa_AF", "FA").forEach {
            assertEquals("fa", resolveLanguage(AppLanguage.SYSTEM, it))
        }
        listOf("en", "ar", "de", "").forEach {
            assertEquals("en", resolveLanguage(AppLanguage.SYSTEM, it))
        }
    }
    @Test fun explicitSelectionOverridesDevice() {
        assertEquals("en", resolveLanguage(AppLanguage.ENGLISH, "fa"))
        assertEquals("fa", resolveLanguage(AppLanguage.PERSIAN, "en"))
        assertEquals("fa", resolveLanguage(AppLanguage.SYSTEM, "fa"))
    }
}
