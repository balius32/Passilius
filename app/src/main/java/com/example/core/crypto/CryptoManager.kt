package com.example.core.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object CryptoManager {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val MASTER_VAULT_KEY_ALIAS = "VaultMasterKeystoreKey_v2"
    private const val AES_GCM_TRANSFORMATION = "AES/GCM/NoPadding"
    private const val GCM_TAG_LENGTH = 128
    private const val GCM_IV_LENGTH = 12

    private var keyStore: KeyStore? = null
    private var fallbackKey: SecretKey? = null

    init {
        try {
            val ks = KeyStore.getInstance(ANDROID_KEYSTORE)
            ks.load(null)
            keyStore = ks
            ensureMasterKeyExists()
        } catch (e: Exception) {
            // JVM / Robolectric environment fallback: Derive an in-memory 256-bit AES key
            val digest = MessageDigest.getInstance("SHA-256")
            val keyBytes = digest.digest("VaultLocalJvmTestMasterKeyFallback".toByteArray(Charsets.UTF_8))
            fallbackKey = SecretKeySpec(keyBytes, "AES")
        }
    }

    private fun ensureMasterKeyExists() {
        val ks = keyStore ?: return
        if (!ks.containsAlias(MASTER_VAULT_KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            )
            val spec = KeyGenParameterSpec.Builder(
                MASTER_VAULT_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setUserAuthenticationRequired(false)
                .build()

            keyGenerator.init(spec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val ks = keyStore
        return if (ks != null && ks.containsAlias(MASTER_VAULT_KEY_ALIAS)) {
            ks.getKey(MASTER_VAULT_KEY_ALIAS, null) as SecretKey
        } else {
            fallbackKey ?: SecretKeySpec(ByteArray(32), "AES")
        }
    }

    /**
     * Encrypts plaintext string using AES-256-GCM.
     * Returns a composite string: Base64(IV):Base64(Ciphertext)
     */
    fun encrypt(plainText: String): String {
        if (plainText.isEmpty()) return ""
        val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv ?: ByteArray(GCM_IV_LENGTH).also { SecureRandom().nextBytes(it) }
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        val encodedIv = Base64.encodeToString(iv, Base64.NO_WRAP)
        val encodedCiphertext = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        return "$encodedIv:$encodedCiphertext"
    }

    /**
     * Decrypts composite string: Base64(IV):Base64(Ciphertext) using AES-256-GCM.
     */
    fun decrypt(encryptedPayload: String): String {
        if (encryptedPayload.isEmpty()) return ""
        val parts = encryptedPayload.split(":")
        if (parts.size != 2) {
            // Fallback for unencrypted legacy or raw text
            return encryptedPayload
        }

        return try {
            val iv = Base64.decode(parts[0], Base64.NO_WRAP)
            val cipherTextBytes = Base64.decode(parts[1], Base64.NO_WRAP)
            val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)

            val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), gcmSpec)
            val decryptedBytes = cipher.doFinal(cipherTextBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            // Return raw payload if it fails to decode as GCM
            encryptedPayload
        }
    }

    /**
     * Derives a cryptographic hash from Master Password using PBKDF2
     */
    fun deriveMasterKeyHash(passcode: String, salt: ByteArray): String {
        val iterations = 10000
        val keyLength = 256
        val spec = PBEKeySpec(passcode.toCharArray(), salt, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }

    fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return salt
    }
}
