package com.example.core.crypto

import java.io.File

internal fun vaultDir(): File {
    val home = System.getProperty("user.home")
    return File(home, ".passilius-vault").also { it.mkdirs() }
}
