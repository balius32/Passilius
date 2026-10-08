package com.example.core.platform

import androidx.compose.runtime.staticCompositionLocalOf

data class PlatformCapabilities(
    val supportsBiometricUnlock: Boolean,
    val canHostDeviceSync: Boolean = false,
    val canScanSyncQr: Boolean = false
)

val LocalPlatformCapabilities = staticCompositionLocalOf {
    PlatformCapabilities(supportsBiometricUnlock = false)
}

expect fun platformCapabilities(): PlatformCapabilities
