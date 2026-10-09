package com.example.core.crypto

import java.io.File
import java.util.Base64

actual object LegacyVaultCrypto {
    private fun keyFile(): File = File(vaultDir(), "vault.key")

    actual fun hasLegacyKey(): Boolean = keyFile().exists()

    actual fun decrypt(payload: String): String? {
        val file = keyFile()
        if (!file.exists()) return null
        return try {
            val key = Base64.getDecoder().decode(file.readText().trim())
            CryptoEngine.decrypt(key, payload)
        } catch (_: Exception) {
            null
        }
    }

    actual fun destroy() {
        keyFile().delete()
    }
}
