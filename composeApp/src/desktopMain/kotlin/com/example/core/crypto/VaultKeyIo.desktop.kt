package com.example.core.crypto

import java.io.File
import java.io.FileOutputStream
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

actual object VaultKeyIo {
    private fun wrapFile(): File = File(vaultDir(), "vault.wrap")

    actual fun readWrap(): String? {
        val file = wrapFile()
        if (!file.exists()) return null
        return file.readText()
    }

    actual fun writeWrap(contents: String) {
        val target = wrapFile()
        val parent = target.parentFile ?: error("Vault key path has no directory")
        parent.mkdirs()
        val tmp = File(parent, "${target.name}.tmp")
        try {
            FileOutputStream(tmp).use { out ->
                out.write(contents.toByteArray(Charsets.UTF_8))
                out.channel.force(true)
            }
            val source = tmp.toPath()
            val destination = target.toPath()
            try {
                Files.move(
                    source,
                    destination,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
                )
            } catch (_: AtomicMoveNotSupportedException) {
                Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (error: Exception) {
            tmp.delete()
            throw error
        }
    }
}
