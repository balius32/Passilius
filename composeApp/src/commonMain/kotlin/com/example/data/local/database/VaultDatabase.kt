package com.example.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.CredentialDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CredentialEntity
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [CredentialEntity::class, CategoryEntity::class],
    version = 4,
    exportSchema = true
)
@ConstructedBy(VaultDatabaseConstructor::class)
abstract class VaultDatabase : RoomDatabase() {
    abstract fun credentialDao(): CredentialDao
    abstract fun categoryDao(): CategoryDao
}

@Suppress("KotlinNoActualForExpect")
expect object VaultDatabaseConstructor : RoomDatabaseConstructor<VaultDatabase> {
    override fun initialize(): VaultDatabase
}

expect fun createVaultDatabaseBuilder(): RoomDatabase.Builder<VaultDatabase>

fun createVaultDatabase(): VaultDatabase {
    return createVaultDatabaseBuilder()
        .fallbackToDestructiveMigration(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
