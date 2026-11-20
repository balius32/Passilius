package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.core.biometric.BiometricSettings
import com.example.core.crypto.VaultFiles
import com.example.core.biometric.BiometricUnlock
import com.example.core.util.ClipboardHelper
import com.example.core.util.ClipboardSettings
import com.example.data.local.database.bindVaultDatabaseContext
import com.example.di.AppGraph

class MainActivity : AppCompatActivity() {

    private lateinit var appGraph: AppGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.core.localization.LanguagePreferences.bind(applicationContext)
        com.example.core.localization.LanguageController.initialize()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        bindVaultDatabaseContext(applicationContext)
        VaultFiles.bind(applicationContext)
        ClipboardHelper.bind(applicationContext)
        ClipboardSettings.bind(applicationContext)
        BiometricUnlock.bind(this)
        BiometricSettings.bind(applicationContext)

        appGraph = AppGraph.create()

        setContent {
            App(appGraph = appGraph)
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        com.example.core.localization.LanguageController.refreshSystemLanguage()
    }
}
