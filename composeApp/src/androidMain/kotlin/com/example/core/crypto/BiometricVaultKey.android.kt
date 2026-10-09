package com.example.core.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

actual object BiometricVaultKey {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val ALIAS = "VaultBiometricWrap_v1"
    private const val TRANSFORM = "AES/GCM/NoPadding"
    private const val TAG_BITS = 128

    actual fun isEnrolled(): Boolean {
        val file = VaultFiles.biometricFile()
        return file.exists() && file.length() > 0 && loadKey() != null
    }

    actual fun enroll() {
        val dek = VaultSession.requireKey()
        val key = ensureKey()
        val cipher = Cipher.getInstance(TRANSFORM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val ciphertext = cipher.doFinal(dek)
        val payload = Base64.encodeToString(iv, Base64.NO_WRAP) + ":" +
            Base64.encodeToString(ciphertext, Base64.NO_WRAP)
        val file = VaultFiles.biometricFile()
        file.parentFile?.mkdirs()
        file.writeText(payload)
    }

    actual fun clear() {
        VaultFiles.biometricFile().delete()
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            if (keyStore.containsAlias(ALIAS)) keyStore.deleteEntry(ALIAS)
        } catch (_: Exception) {
            Unit
        }
    }

    actual fun unwrapAfterAuth(): Boolean {
        val key = loadKey() ?: return false
        val file = VaultFiles.biometricFile()
        if (!file.exists()) return false
        val parts = file.readText().trim().split(':')
        if (parts.size != 2) return false
        return try {
            val iv = Base64.decode(parts[0], Base64.NO_WRAP)
            val ciphertext = Base64.decode(parts[1], Base64.NO_WRAP)
            val cipher = Cipher.getInstance(TRANSFORM)
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_BITS, iv))
            val dek = cipher.doFinal(ciphertext)
            VaultSession.unlock(dek)
            dek.fill(0)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun ensureKey(): SecretKey {
        loadKey()?.let { return it }
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val builder = KeyGenParameterSpec.Builder(
            ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setUserAuthenticationRequired(true)
        @Suppress("DEPRECATION")
        builder.setUserAuthenticationValidityDurationSeconds(30)
        generator.init(builder.build())
        return generator.generateKey()
    }

    private fun loadKey(): SecretKey? = try {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        if (!keyStore.containsAlias(ALIAS)) null
        else keyStore.getKey(ALIAS, null) as? SecretKey
    } catch (_: Exception) {
        null
    }
}
