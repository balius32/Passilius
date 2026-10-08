package com.example.core.sync

expect object SyncEnvelope {
    fun encrypt(token: ByteArray, plaintext: ByteArray): String
    fun decrypt(token: ByteArray, payload: String): ByteArray
}
