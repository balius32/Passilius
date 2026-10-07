package com.example.core.biometric

sealed interface BiometricUnlockResult {
    data object Success : BiometricUnlockResult
    data object Unavailable : BiometricUnlockResult
    data class Error(val message: String) : BiometricUnlockResult
}

expect object BiometricUnlock {
    fun isAvailable(): Boolean
    fun authenticate(
        title: String,
        subtitle: String,
        onResult: (BiometricUnlockResult) -> Unit
    )
}
