package com.example.core.crypto

expect object BiometricVaultKey {
    fun isEnrolled(): Boolean
    fun enroll()
    fun clear()
    fun unwrapAfterAuth(): Boolean
}
