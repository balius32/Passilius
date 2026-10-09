package com.example.core.biometric

import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
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
        return manager.canAuthenticate(authenticators()) == BiometricManager.BIOMETRIC_SUCCESS
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
        if (manager.canAuthenticate(authenticators()) != BiometricManager.BIOMETRIC_SUCCESS) {
            onResult(BiometricUnlockResult.Unavailable)
            return
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
                    val canceled = errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                        errorCode == BiometricPrompt.ERROR_CANCELED ||
                        errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON
                    onResult(
                        if (canceled) BiometricUnlockResult.Error("")
                        else BiometricUnlockResult.Error(errString.toString())
                    )
                }

                override fun onAuthenticationFailed() {
                    // Keep the system prompt open for another attempt.
                }
            }
        )

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(authenticators())
            .apply {
                // Device credential cannot share a negative button, and that
                // combination is only valid from API 30.
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    setNegativeButtonText("Cancel")
                }
            }
            .build()

        try {
            prompt.authenticate(info)
        } catch (error: IllegalArgumentException) {
            onResult(BiometricUnlockResult.Error(error.message ?: "Biometrics unavailable"))
        } catch (error: IllegalStateException) {
            onResult(BiometricUnlockResult.Error(error.message ?: "Biometrics unavailable"))
        }
    }

    private fun authenticators(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            BIOMETRIC_WEAK or DEVICE_CREDENTIAL
        } else {
            BIOMETRIC_WEAK
        }
    }
}
