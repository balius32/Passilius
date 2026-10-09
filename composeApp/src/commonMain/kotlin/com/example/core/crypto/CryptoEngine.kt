package com.example.core.crypto

expect object CryptoEngine {
    fun randomBytes(size: Int): ByteArray
    fun pbkdf2(password: CharArray, salt: ByteArray, iterations: Int): ByteArray
    fun encrypt(key: ByteArray, plainText: String): String
    fun decrypt(key: ByteArray, payload: String): String
    fun encryptBytes(key: ByteArray, plain: ByteArray): String
    fun decryptBytes(key: ByteArray, payload: String): ByteArray
}
