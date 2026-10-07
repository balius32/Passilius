package com.example.core.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

actual object BiometricUnlock {

    @Volatile
    private var hostActivity: FragmentActivity? = null

    fun bind(activity: FragmentActivity) {
        hostActivity = activity
    }

    actual fun isAvailable(): Boolean {
        val activity = hostActivity ?: return false
        val manager = BiometricManager.from(activity)
        return manager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) ==
            BiometricManager.BIOMETRIC_SUCCESS
    }

    actual fun authenticate(
        title: String,
        subtitle: String,
        onResult: (BiometricUnlockResult) -> Unit
    ) {
        val activity = hostActivity
        if (activity == null) {
            onResult(BiometricUnlockResult.Unavailable)
            return
        }

        val manager = BiometricManager.from(activity)
        when (manager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> Unit
            else -> {
                onResult(BiometricUnlockResult.Unavailable)
                return
            }
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val prompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    onResult(BiometricUnlockResult.Success)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onResult(BiometricUnlockResult.Error(errString.toString()))
                }

                override fun onAuthenticationFailed() {
                    // Keep waiting for another attempt.
                }
            }
        )

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        prompt.authenticate(info)
    }
}
