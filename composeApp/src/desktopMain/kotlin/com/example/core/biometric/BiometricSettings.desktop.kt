package com.example.core.biometric

actual object BiometricSettings {
    actual fun isUnlockEnabled(): Boolean = false
    actual fun setUnlockEnabled(enabled: Boolean) = Unit
}
