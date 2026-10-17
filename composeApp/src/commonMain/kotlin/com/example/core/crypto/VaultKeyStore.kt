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
        if (masterPassword.isEmpty()) return@withContext false
        val record = decodeRecord(VaultKeyIo.readWrap() ?: return@withContext false)
            ?: return@withContext false
        val chars = masterPassword.toCharArray()
        val kek = try {
            CryptoEngine.pbkdf2(chars, record.salt, record.iterations)
        } catch (_: Exception) {
            return@withContext false
        } finally {
            chars.fill('\u0000')
        }
        try {
            val dek = CryptoEngine.decryptBytes(kek, record.wrappedDek)
            VaultSession.unlock(dek)
            dek.fill(0)
            true
        } catch (_: Exception) {
            false
        } finally {
            kek.fill(0)
        }
    }

    fun lock() {
        VaultSession.lock()
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Boolean =
        withContext(Dispatchers.Default) {
            require(newPassword.length >= MIN_PASSWORD_LENGTH) {
                "Master password must be at least $MIN_PASSWORD_LENGTH characters"
            }
            val existing = VaultKeyIo.readWrap() ?: return@withContext false
            val result = rewrapRecord(existing, currentPassword, newPassword, ITERATIONS)
            val dek = result.dek
            if (!result.changed || dek == null) {
                dek?.fill(0)
                return@withContext false
            }
            try {
                VaultKeyIo.writeWrap(result.record)
                VaultSession.unlock(dek)
                true
            } finally {
                dek.fill(0)
            }
        }

    internal fun rewrapRecord(
        recordText: String,
        currentPassword: String,
        newPassword: String,
        iterations: Int
    ): RewrapResult {
        val record = decodeRecord(recordText)
            ?: return RewrapResult(recordText, changed = false, dek = null)
        if (currentPassword.isEmpty()) {
            return RewrapResult(recordText, changed = false, dek = null)
        }
        val currentChars = currentPassword.toCharArray()
        val currentKek = try {
            CryptoEngine.pbkdf2(currentChars, record.salt, record.iterations)
        } catch (_: Exception) {
            return RewrapResult(recordText, changed = false, dek = null)
        } finally {
            currentChars.fill('\u0000')
        }
        val dek = try {
            CryptoEngine.decryptBytes(currentKek, record.wrappedDek)
        } catch (_: VaultCryptoException) {
            return RewrapResult(recordText, changed = false, dek = null)
        } finally {
            currentKek.fill(0)
        }
        val salt = CryptoEngine.randomBytes(16)
        val newChars = newPassword.toCharArray()
        val newKek = CryptoEngine.pbkdf2(newChars, salt, iterations)
        newChars.fill('\u0000')
        return try {
            val wrapped = CryptoEngine.encryptBytes(newKek, dek)
            RewrapResult(
                record = encodeRecord(iterations, salt, wrapped),
                changed = true,
                dek = dek
            )
        } catch (error: Exception) {
            dek.fill(0)
            throw error
        } finally {
            newKek.fill(0)
        }
    }

    internal fun createWrapRecord(password: String, iterations: Int, dek: ByteArray): String {
        val salt = CryptoEngine.randomBytes(16)
        val chars = password.toCharArray()
        val kek = CryptoEngine.pbkdf2(chars, salt, iterations)
        chars.fill('\u0000')
        return try {
            encodeRecord(iterations, salt, CryptoEngine.encryptBytes(kek, dek))
        } finally {
            kek.fill(0)
        }
    }

    internal fun unwrapRecord(recordText: String, password: String): ByteArray? {
        if (password.isEmpty()) return null
        val record = decodeRecord(recordText) ?: return null
        val chars = password.toCharArray()
        val kek = try {
            CryptoEngine.pbkdf2(chars, record.salt, record.iterations)
        } catch (_: Exception) {
            return null
        } finally {
            chars.fill('\u0000')
        }
        return try {
            CryptoEngine.decryptBytes(kek, record.wrappedDek)
        } catch (_: VaultCryptoException) {
            null
        } finally {
            kek.fill(0)
        }
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

internal class RewrapResult(
    val record: String,
    val changed: Boolean,
    val dek: ByteArray?
)

private data class WrapRecord(
    val iterations: Int,
    val salt: ByteArray,
    val wrappedDek: String
)

internal expect fun base64Encode(bytes: ByteArray): String
internal expect fun base64Decode(text: String): ByteArray?
