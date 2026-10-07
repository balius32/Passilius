package com.example.core.crypto

import java.io.File
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

actual object CryptoManager {

    private const val AES_GCM_TRANSFORMATION = "AES/GCM/NoPadding"
    private const val GCM_TAG_LENGTH = 128
    private const val GCM_IV_LENGTH = 12

    private val secretKey: SecretKey by lazy { loadOrCreateKey() }

    private fun vaultDir(): File {
        val home = System.getProperty("user.home")
        return File(home, ".passilius-vault").also { it.mkdirs() }
    }

    private fun loadOrCreateKey(): SecretKey {
        val keyFile = File(vaultDir(), "vault.key")
        if (keyFile.exists()) {
            val bytes = Base64.getDecoder().decode(keyFile.readText().trim())
            return SecretKeySpec(bytes, "AES")
        }
        val generator = KeyGenerator.getInstance("AES")
        generator.init(256)
        val key = generator.generateKey()
        keyFile.writeText(Base64.getEncoder().encodeToString(key.encoded))
        return key
    }

    actual fun encrypt(plainText: String): String {
        if (plainText.isEmpty()) return ""
        val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv ?: ByteArray(GCM_IV_LENGTH).also { SecureRandom().nextBytes(it) }
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val encodedIv = Base64.getEncoder().encodeToString(iv)
        val encodedCiphertext = Base64.getEncoder().encodeToString(encryptedBytes)
        return "$encodedIv:$encodedCiphertext"
    }

    actual fun decrypt(encryptedPayload: String): String {
        if (encryptedPayload.isEmpty()) return ""
        val parts = encryptedPayload.split(":")
        if (parts.size != 2) return encryptedPayload
        return try {
            val iv = Base64.getDecoder().decode(parts[0])
            val cipherTextBytes = Base64.getDecoder().decode(parts[1])
            val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)
            String(cipher.doFinal(cipherTextBytes), Charsets.UTF_8)
        } catch (_: Exception) {
            encryptedPayload
        }
    }

    actual fun deriveMasterKeyHash(passcode: String, salt: ByteArray): String {
        val spec = PBEKeySpec(passcode.toCharArray(), salt, 10000, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        return Base64.getEncoder().encodeToString(hash)
    }

    actual fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return salt
    }
}
