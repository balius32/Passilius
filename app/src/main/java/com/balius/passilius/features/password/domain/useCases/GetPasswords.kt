package com.balius.passilius.features.password.domain.useCases

import com.balius.passilius.features.password.domain.model.Password
import com.balius.passilius.features.password.domain.repository.PasswordRepository
import kotlinx.coroutines.flow.Flow

class GetPasswords (private val repository: PasswordRepository) {
    operator fun invoke(): Flow<List<Password>> = repository.getPasswords()
}