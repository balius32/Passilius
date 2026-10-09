package com.example.core.crypto

import java.io.File

actual object VaultKeyIo {
    private fun wrapFile(): File = File(vaultDir(), "vault.wrap")

    actual fun readWrap(): String? {
        val file = wrapFile()
        if (!file.exists()) return null
        return file.readText()
    }

    actual fun writeWrap(contents: String) {
        wrapFile().writeText(contents)
    }
}
