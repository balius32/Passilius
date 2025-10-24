package com.balius.passilius.features.password.data.dataSource

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.balius.passilius.core.app.App.Companion.applicationContext
import com.balius.passilius.features.password.data.models.PasswordEntity
import com.balius.passilius.features.password.domain.model.Password

@Database (entities = [PasswordEntity::class] , version = 1)
abstract class DataBase : RoomDatabase() {

    abstract  fun passwordDao() : PasswordDao


}