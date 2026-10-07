package com.example.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private lateinit var appContext: Context

fun bindVaultDatabaseContext(context: Context) {
    appContext = context.applicationContext
}

actual fun createVaultDatabaseBuilder(): RoomDatabase.Builder<VaultDatabase> {
    val dbFile = appContext.getDatabasePath("vault_database.db")
    return Room.databaseBuilder<VaultDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
