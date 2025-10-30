package com.balius.passilius.features.password.data.repositoryImpl

import com.balius.passilius.features.password.data.dataSource.PasswordDao
import com.balius.passilius.features.password.data.models.PasswordEntity
import com.balius.passilius.features.password.domain.model.Password
import com.balius.passilius.features.password.domain.repository.PasswordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PasswordRepositoryImpl(
    private val dao: PasswordDao
) : PasswordRepository {

    override suspend fun savePassword(password: Password) {
        dao.insertPassword(password.toEntity())
    }

    override fun getPasswords(): Flow<List<Password>> {
        return dao.getAllPasswords().map { list ->
            list.map { it.toDomain() }
        }
    }
}


// Mapping extensions
fun PasswordEntity.toDomain() = Password(id, user, password, url)
fun Password.toEntity() = PasswordEntity(user = username, password = password, url = url)