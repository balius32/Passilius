package com.example.core.platform

actual fun platformCapabilities(): PlatformCapabilities =
    PlatformCapabilities(
        supportsBiometricUnlock = false,
        canHostDeviceSync = true,
        canScanSyncQr = false
    )
