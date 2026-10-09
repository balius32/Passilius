package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.core.biometric.BiometricSettings
import com.example.core.biometric.BiometricUnlock
import com.example.core.util.ClipboardHelper
import com.example.data.local.database.bindVaultDatabaseContext
import com.example.di.AppGraph

class MainActivity : AppCompatActivity() {

    private lateinit var appGraph: AppGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        bindVaultDatabaseContext(applicationContext)
        ClipboardHelper.bind(applicationContext)
        BiometricUnlock.bind(this)
        BiometricSettings.bind(applicationContext)

        appGraph = AppGraph.create()

        setContent {
            App(appGraph = appGraph)
        }
    }
}
