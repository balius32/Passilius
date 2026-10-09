package com.example.core.biometric

expect object BiometricSettings {
    fun isUnlockEnabled(): Boolean
    fun setUnlockEnabled(enabled: Boolean)
}
