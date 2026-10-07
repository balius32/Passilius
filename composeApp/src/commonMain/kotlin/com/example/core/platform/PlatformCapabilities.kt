package com.example.core.platform

import androidx.compose.runtime.staticCompositionLocalOf

data class PlatformCapabilities(
    val supportsBiometricUnlock: Boolean
)

val LocalPlatformCapabilities = staticCompositionLocalOf {
    PlatformCapabilities(supportsBiometricUnlock = false)
}

expect fun platformCapabilities(): PlatformCapabilities
