package com.example.core.crypto

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CryptoEngineTest {

    @Test
    fun encryptDecryptRoundTrip() {
        val key = CryptoEngine.randomBytes(32)
        val sealed = CryptoEngine.encrypt(key, "vault-secret")
        assertEquals("vault-secret", CryptoEngine.decrypt(key, sealed))
    }

    @Test
    fun wrongKeyThrows() {
        val sealed = CryptoEngine.encrypt(CryptoEngine.randomBytes(32), "vault-secret")
        assertFailsWith<VaultCryptoException> {
            CryptoEngine.decrypt(CryptoEngine.randomBytes(32), sealed)
        }
    }

    @Test
    fun passwordWrapRoundTrip() {
        val salt = CryptoEngine.randomBytes(16)
        val dek = CryptoEngine.randomBytes(32)
        val kek = CryptoEngine.pbkdf2("correct horse".toCharArray(), salt, 1_000)
        val wrapped = CryptoEngine.encryptBytes(kek, dek)
        val opened = CryptoEngine.decryptBytes(kek, wrapped)
        assertEquals(dek.toList(), opened.toList())
        kek.fill(0)
    }
}
