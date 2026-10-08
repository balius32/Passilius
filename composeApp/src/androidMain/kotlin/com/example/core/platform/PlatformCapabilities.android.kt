package com.example.core.platform

actual fun platformCapabilities(): PlatformCapabilities =
    PlatformCapabilities(
        supportsBiometricUnlock = true,
        canHostDeviceSync = false,
        canScanSyncQr = true
    )
