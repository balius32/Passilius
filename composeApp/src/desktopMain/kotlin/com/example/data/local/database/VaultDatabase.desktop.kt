package com.example.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun createVaultDatabaseBuilder(): RoomDatabase.Builder<VaultDatabase> {
    val dir = File(System.getProperty("user.home"), ".passilius-vault").also { it.mkdirs() }
    val dbFile = File(dir, "vault_database.db")
    return Room.databaseBuilder<VaultDatabase>(name = dbFile.absolutePath)
}
