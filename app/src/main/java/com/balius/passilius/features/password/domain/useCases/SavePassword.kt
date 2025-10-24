package com.balius.passilius.features.password.domain.useCases

import com.balius.passilius.features.password.domain.model.Password
import com.balius.passilius.features.password.domain.repository.PasswordRepository

class SavePassword(
    private val repository: PasswordRepository
) {
    suspend operator fun invoke(password: Password) {
        repository.savePassword(password)
    }
}
