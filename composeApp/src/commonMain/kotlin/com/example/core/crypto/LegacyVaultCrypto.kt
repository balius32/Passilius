package com.example.core.crypto

expect object LegacyVaultCrypto {
    fun hasLegacyKey(): Boolean
    fun decrypt(payload: String): String?
    fun destroy()
}
