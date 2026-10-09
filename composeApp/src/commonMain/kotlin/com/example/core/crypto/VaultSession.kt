package com.example.core.crypto

object VaultSession {
    private var dek: ByteArray? = null

    fun isUnlocked(): Boolean = dek != null

    fun requireKey(): ByteArray = dek ?: throw VaultCryptoException("Vault is locked")

    fun unlock(key: ByteArray) {
        dek?.fill(0)
        dek = key.copyOf()
    }

    fun lock() {
        dek?.fill(0)
        dek = null
    }
}
