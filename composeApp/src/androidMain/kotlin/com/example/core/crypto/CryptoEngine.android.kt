package com.example.core.crypto

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

actual object CryptoEngine {
    private const val TRANSFORM = "AES/GCM/NoPadding"
    private const val TAG_BITS = 128

    actual fun randomBytes(size: Int): ByteArray =
        ByteArray(size).also { SecureRandom().nextBytes(it) }

    actual fun pbkdf2(password: CharArray, salt: ByteArray, iterations: Int): ByteArray {
        val spec = PBEKeySpec(password, salt, iterations, 256)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    actual fun encrypt(key: ByteArray, plainText: String): String =
        encryptBytes(key, plainText.encodeToByteArray())

    actual fun decrypt(key: ByteArray, payload: String): String =
        String(decryptBytes(key, payload), Charsets.UTF_8)

    actual fun encryptBytes(key: ByteArray, plain: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORM)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"))
        val iv = cipher.iv
        val ciphertext = cipher.doFinal(plain)
        return base64(iv) + ":" + base64(ciphertext)
    }

    actual fun decryptBytes(key: ByteArray, payload: String): ByteArray {
        val parts = payload.split(':')
        if (parts.size != 2) throw VaultCryptoException("Invalid ciphertext")
        return try {
            val iv = Base64.decode(parts[0], Base64.NO_WRAP)
            val ciphertext = Base64.decode(parts[1], Base64.NO_WRAP)
            val cipher = Cipher.getInstance(TRANSFORM)
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(key, "AES"),
                GCMParameterSpec(TAG_BITS, iv)
            )
            cipher.doFinal(ciphertext)
        } catch (error: VaultCryptoException) {
            throw error
        } catch (error: Exception) {
            throw VaultCryptoException(error.message ?: "Decrypt failed")
        }
    }

    private fun base64(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.NO_WRAP)
}

internal actual fun base64Encode(bytes: ByteArray): String =
    Base64.encodeToString(bytes, Base64.NO_WRAP)

internal actual fun base64Decode(text: String): ByteArray? = try {
    Base64.decode(text, Base64.NO_WRAP)
} catch (_: Exception) {
    null
}
