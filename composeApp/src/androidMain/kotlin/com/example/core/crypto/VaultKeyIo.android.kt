package com.example.core.crypto

import android.system.ErrnoException
import android.system.Os
import java.io.File
import java.io.FileOutputStream

actual object VaultKeyIo {
    actual fun readWrap(): String? {
        val file = VaultFiles.wrapFile()
        if (!file.exists()) return null
        return file.readText()
    }

    actual fun writeWrap(contents: String) {
        replaceAtomically(VaultFiles.wrapFile(), contents)
    }

    private fun replaceAtomically(target: File, contents: String) {
        val parent = target.parentFile ?: error("Vault key path has no directory")
        parent.mkdirs()
        val tmp = File(parent, "${target.name}.tmp")
        try {
            FileOutputStream(tmp).use { out ->
                out.write(contents.toByteArray(Charsets.UTF_8))
                out.channel.force(true)
            }
            try {
                Os.rename(tmp.absolutePath, target.absolutePath)
            } catch (error: ErrnoException) {
                throw IllegalStateException("Could not replace the vault key", error)
            }
        } catch (error: Exception) {
            tmp.delete()
            throw error
        }
    }
}
