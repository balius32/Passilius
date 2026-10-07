package com.example.core.crypto

expect object CryptoManager {
    fun encrypt(plainText: String): String
    fun decrypt(encryptedPayload: String): String
    fun deriveMasterKeyHash(passcode: String, salt: ByteArray): String
    fun generateSalt(): ByteArray
}
