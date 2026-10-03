package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credentials")
data class CredentialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val service: String,
    val username: String,
    val encryptedPassword: String,
    val category: String,
    val websiteUrl: String,
    val encryptedTotpSecret: String,
    val notes: String,
    val iconKey: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isFavorite: Boolean,
    val entropyBits: Int
)
