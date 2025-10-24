package com.balius.passilius.core.di

import androidx.room.Room
import com.balius.passilius.features.password.data.dataSource.DataBase
import com.balius.passilius.features.password.data.models.PasswordEntity
import com.balius.passilius.features.password.data.repositoryImpl.PasswordRepositoryImpl
import com.balius.passilius.features.password.domain.repository.PasswordRepository
import com.balius.passilius.features.password.domain.useCases.GetPasswords
import com.balius.passilius.features.password.domain.useCases.PasswordUseCases
import com.balius.passilius.features.password.domain.useCases.SavePassword
import com.balius.passilius.features.password.presentation.crud.PasswordViewModel
import com.balius.passilius.features.password.presentation.home.presentation.HomeEvent
import com.balius.passilius.features.password.presentation.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            get(),
            DataBase::class.java,
            "tbl_pass"

        ).build()
    }

    single { get<DataBase>().passwordDao() }


    // Repository + UseCase
    single<PasswordRepository> { PasswordRepositoryImpl(get()) }
    single { GetPasswords(get()) }
    single { SavePassword(get()) }

    single {
        PasswordUseCases(
            getPasswords = get(),
            savePassword = get()
        )
    }


    // ViewModel
    viewModel { HomeViewModel(get()) }
    viewModel { PasswordViewModel(get()) }


}
