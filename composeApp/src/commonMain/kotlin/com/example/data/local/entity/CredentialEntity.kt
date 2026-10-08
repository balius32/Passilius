package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "credentials",
    indices = [Index(value = ["syncId"], unique = true)]
)
data class CredentialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val syncId: String,
    val service: String,
    val username: String,
    val encryptedPassword: String,
    val category: String,
    val websiteUrl: String,
    val notes: String,
    val iconKey: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isFavorite: Boolean,
    val entropyBits: Int
)
