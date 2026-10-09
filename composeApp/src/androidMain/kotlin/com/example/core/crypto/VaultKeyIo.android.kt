package com.example.core.crypto

actual object VaultKeyIo {
    actual fun readWrap(): String? {
        val file = VaultFiles.wrapFile()
        if (!file.exists()) return null
        return file.readText()
    }

    actual fun writeWrap(contents: String) {
        val file = VaultFiles.wrapFile()
        file.parentFile?.mkdirs()
        file.writeText(contents)
    }
}
