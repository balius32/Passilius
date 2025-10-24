package com.balius.passilius.features.password.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_pass")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user") val user: String?,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "url") val url: String
)