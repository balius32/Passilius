package com.example.core.crypto

import android.content.Context
import java.io.File

internal object VaultFiles {
    private var appContext: Context? = null

    fun bind(context: Context) {
        appContext = context.applicationContext
    }

    fun wrapFile(): File = File(requireContext().filesDir, "vault.wrap")

    fun biometricFile(): File = File(requireContext().filesDir, "biometric.wrap")

    private fun requireContext(): Context =
        appContext ?: error("Vault storage is not bound")
}
