package com.balius.passilius.features.password.presentation.home.presentation

import com.balius.passilius.features.password.domain.model.Password

sealed class HomeEvent {
     data class Error(val message:String) : HomeEvent()
    object GetPassword : HomeEvent()
}

/*data class GetPassword(val passwordList:List<Password>) : HomeEvent()*/