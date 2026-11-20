package com.example.core.localization

import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import passilius.composeapp.generated.resources.*

class TranslationTest {
    @Test fun messagesResolveAgainAfterLocaleChanges() {
        val previous = Locale.getDefault()
        try {
            val message = UiMessage(Res.string.passwords_do_not_match)
            Locale.setDefault(Locale.ENGLISH)
            assertEquals("Passwords do not match", message.resolve())
            Locale.setDefault(Locale.forLanguageTag("fa"))
            assertEquals("رمزها یکسان نیستند", message.resolve())
            Locale.setDefault(Locale.ENGLISH)
            assertEquals("Passwords do not match", message.resolve())
        } finally { Locale.setDefault(previous) }
    }

    @Test fun formattedResourcesPreserveUserText() {
        val previous = Locale.getDefault()
        val mixed = "کاربر@example.com/AbC123"
        try {
            Locale.setDefault(Locale.forLanguageTag("fa"))
            assertEquals("نشانی‌ها: $mixed", tr(Res.string.addresses, mixed))
            assertNotEquals("Language", tr(Res.string.language))
        } finally { Locale.setDefault(previous) }
    }
}
