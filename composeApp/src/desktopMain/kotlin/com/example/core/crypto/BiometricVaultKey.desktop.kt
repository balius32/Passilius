package com.example.core.crypto

actual object BiometricVaultKey {
    actual fun isEnrolled(): Boolean = false
    actual fun enroll() = Unit
    actual fun clear() = Unit
    actual fun unwrapAfterAuth(): Boolean = false
}
