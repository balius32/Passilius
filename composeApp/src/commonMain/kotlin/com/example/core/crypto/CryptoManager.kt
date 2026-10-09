package com.example.core.crypto

object CryptoManager {
    fun encrypt(plainText: String): String {
        if (plainText.isEmpty()) return ""
        return CryptoEngine.encrypt(VaultSession.requireKey(), plainText)
    }

    fun decrypt(encryptedPayload: String): String {
        if (encryptedPayload.isEmpty()) return ""
        return CryptoEngine.decrypt(VaultSession.requireKey(), encryptedPayload)
    }
}
