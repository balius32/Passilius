package com.balius.passilius.features.password.domain.repository

import com.balius.passilius.features.password.domain.model.Password
import kotlinx.coroutines.flow.Flow

interface PasswordRepository {
    suspend fun savePassword(password: Password)
    fun getPasswords(): Flow<List<Password>>
}