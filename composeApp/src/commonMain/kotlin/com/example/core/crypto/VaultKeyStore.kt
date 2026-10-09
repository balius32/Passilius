package com.example.core.crypto

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object VaultKeyStore {
    const val MIN_PASSWORD_LENGTH = 8
    private const val ITERATIONS = 600_000

    fun isInitialized(): Boolean = !VaultKeyIo.readWrap().isNullOrBlank()

    suspend fun create(masterPassword: String) = withContext(Dispatchers.Default) {
        require(masterPassword.length >= MIN_PASSWORD_LENGTH) {
            "Master password must be at least $MIN_PASSWORD_LENGTH characters"
        }
        val salt = CryptoEngine.randomBytes(16)
        val dek = CryptoEngine.randomBytes(32)
        val chars = masterPassword.toCharArray()
        val kek = CryptoEngine.pbkdf2(chars, salt, ITERATIONS)
        chars.fill('\u0000')
        try {
            val wrapped = CryptoEngine.encryptBytes(kek, dek)
            VaultKeyIo.writeWrap(encodeRecord(ITERATIONS, salt, wrapped))
            VaultSession.unlock(dek)
            dek.fill(0)
        } finally {
            kek.fill(0)
        }
    }

    suspend fun unlock(masterPassword: String): Boolean = withContext(Dispatchers.Default) {
        val record = decodeRecord(VaultKeyIo.readWrap() ?: return@withContext false)
            ?: return@withContext false
        val chars = masterPassword.toCharArray()
        val kek = CryptoEngine.pbkdf2(chars, record.salt, record.iterations)
        chars.fill('\u0000')
        try {
            val dek = CryptoEngine.decryptBytes(kek, record.wrappedDek)
            VaultSession.unlock(dek)
            dek.fill(0)
            true
        } catch (_: VaultCryptoException) {
            false
        } finally {
            kek.fill(0)
        }
    }

    fun lock() {
        VaultSession.lock()
    }

    private fun encodeRecord(iterations: Int, salt: ByteArray, wrappedDek: String): String =
        "v1\n$iterations\n${base64Encode(salt)}\n$wrappedDek"

    private fun decodeRecord(text: String): WrapRecord? {
        val lines = text.trim().lines()
        if (lines.size < 4 || lines[0] != "v1") return null
        val iterations = lines[1].toIntOrNull() ?: return null
        val salt = base64Decode(lines[2]) ?: return null
        val wrapped = lines[3]
        if (wrapped.isBlank()) return null
        return WrapRecord(iterations, salt, wrapped)
    }
}

private data class WrapRecord(
    val iterations: Int,
    val salt: ByteArray,
    val wrappedDek: String
)

internal expect fun base64Encode(bytes: ByteArray): String
internal expect fun base64Decode(text: String): ByteArray?
