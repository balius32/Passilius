package com.example.core.biometric

actual object BiometricUnlock {
    actual fun isAvailable(): Boolean = false

    actual fun authenticate(
        title: String,
        subtitle: String,
        onResult: (BiometricUnlockResult) -> Unit
    ) {
        onResult(BiometricUnlockResult.Unavailable)
    }
}
