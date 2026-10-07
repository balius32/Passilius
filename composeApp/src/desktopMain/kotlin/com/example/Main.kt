package com.example

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.di.AppGraph

fun main() = application {
    val appGraph = AppGraph.create()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Passilius",
        state = rememberWindowState(width = 420.dp, height = 840.dp)
    ) {
        App(appGraph = appGraph)
    }
}
