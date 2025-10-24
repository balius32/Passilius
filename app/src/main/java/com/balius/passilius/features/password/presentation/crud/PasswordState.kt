package com.balius.passilius.features.password.presentation.crud

data class PasswordState (
    val username: String = "",
    val password: String = "",
    val url: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false

)

