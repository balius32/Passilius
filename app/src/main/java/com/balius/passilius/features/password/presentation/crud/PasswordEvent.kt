package com.balius.passilius.features.password.presentation.crud

sealed class PasswordEvent {
    data class EnteredUsername(val value: String) : PasswordEvent()
    data class EnteredPassword(val value: String) : PasswordEvent()
    data class EnteredUrl(val value: String) : PasswordEvent()
    object SavePassword : PasswordEvent()
}