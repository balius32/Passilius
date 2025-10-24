package com.balius.passilius.features.password.presentation.home.presentation

import com.balius.passilius.features.password.domain.model.Password

data class HomeState(
    val passwords: List<Password> = emptyList(),

    val isLoading: Boolean = false,

    val error: String? = null
)