package com.example.core.sync

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

actual object SyncEnvelope {
    private const val TRANSFORM = "AES/GCM/NoPadding"
    private const val GCM_TAG_BITS = 128
    private const val IV_LENGTH = 12

    actual fun encrypt(token: ByteArray, plaintext: ByteArray): String {
        require(token.size == 32) { "Sync token must be 32 bytes" }
        val key = SecretKeySpec(token, "AES")
        val cipher = Cipher.getInstance(TRANSFORM)
        val iv = ByteArray(IV_LENGTH).also { SecureRandom().nextBytes(it) }
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(GCM_TAG_BITS, iv))
        val ciphertext = cipher.doFinal(plaintext)
        val packed = iv + ciphertext
        return Base64.encodeToString(packed, Base64.NO_WRAP)
    }

    actual fun decrypt(token: ByteArray, payload: String): ByteArray {
        require(token.size == 32) { "Sync token must be 32 bytes" }
        val packed = Base64.decode(payload, Base64.NO_WRAP)
        require(packed.size > IV_LENGTH) { "Invalid sync payload" }
        val iv = packed.copyOfRange(0, IV_LENGTH)
        val ciphertext = packed.copyOfRange(IV_LENGTH, packed.size)
        val key = SecretKeySpec(token, "AES")
        val cipher = Cipher.getInstance(TRANSFORM)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(GCM_TAG_BITS, iv))
        return cipher.doFinal(ciphertext)
    }
}
