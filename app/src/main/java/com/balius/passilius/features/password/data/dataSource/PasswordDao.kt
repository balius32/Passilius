package com.balius.passilius.features.password.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.balius.passilius.features.password.data.models.PasswordEntity
import com.balius.passilius.features.password.domain.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: PasswordEntity)

    @Query("SELECT * FROM tbl_pass")
    fun getAllPasswords(): Flow<List<PasswordEntity>>
}