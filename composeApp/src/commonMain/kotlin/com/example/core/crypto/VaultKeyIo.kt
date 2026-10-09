package com.example.core.crypto

expect object VaultKeyIo {
    fun readWrap(): String?
    fun writeWrap(contents: String)
}
