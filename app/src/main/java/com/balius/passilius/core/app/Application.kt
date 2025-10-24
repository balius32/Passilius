package com.balius.passilius.core.app

import android.app.Application
import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.balius.passilius.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Composable
fun MyComposable() {
    val context = LocalContext.current
    val appContext = context.applicationContext

    Text(text = "App name: ${appContext.packageName}")
}


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            // Give Android context to Koin
            androidContext(this@App)

            // Load your Koin modules
            modules(appModule)
        }}


    companion object {
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}