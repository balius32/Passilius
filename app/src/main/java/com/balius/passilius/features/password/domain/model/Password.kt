package com.balius.passilius.features.password.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Password(
    val id : Int?,
    val username: String?,
    val password: String,
    val url: String
)