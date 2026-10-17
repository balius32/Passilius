package com.example.core.crypto

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class VaultKeyRewrapTest {

    @Test
    fun wrongPasswordReturnsOriginalRecord() {
        val dek = CryptoEngine.randomBytes(32)
        val original = VaultKeyStore.createWrapRecord("current-password", 1_000, dek)
        val failed = VaultKeyStore.rewrapRecord(original, "wrong-password", "new-password", 1_000)
        assertFalse(failed.changed)
        assertEquals(original, failed.record)
        assertNull(failed.dek)
        assertEquals(dek.toList(), VaultKeyStore.unwrapRecord(original, "current-password")?.toList())
        dek.fill(0)
    }

    @Test
    fun correctPasswordKeepsTheSameDataKey() {
        val dek = CryptoEngine.randomBytes(32)
        val original = VaultKeyStore.createWrapRecord("current-password", 1_000, dek)
        val next = VaultKeyStore.rewrapRecord(original, "current-password", "new-password", 1_000)
        assertTrue(next.changed)
        assertNotEquals(original, next.record)
        val opened = VaultKeyStore.unwrapRecord(next.record, "new-password")
        assertEquals(dek.toList(), opened?.toList())
        assertEquals(dek.toList(), next.dek?.toList())
        assertNull(VaultKeyStore.unwrapRecord(next.record, "current-password"))
        opened?.fill(0)
        next.dek?.fill(0)
        dek.fill(0)
    }
}
