package com.balius.passilius.features.password.presentation.crud

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balius.passilius.features.password.domain.model.Password
import com.balius.passilius.features.password.domain.useCases.PasswordUseCases
import kotlinx.coroutines.launch

class PasswordViewModel(
    private val useCases: PasswordUseCases
) : ViewModel() {


    private val _state = mutableStateOf(PasswordState())
    val state: State<PasswordState> = _state

    fun onEvent(event: PasswordEvent) {
        when (event) {
            is PasswordEvent.EnteredUsername -> {
                _state.value = _state.value.copy(username = event.value)
            }

            is PasswordEvent.EnteredPassword -> {
                _state.value = _state.value.copy(password = event.value)
            }

            is PasswordEvent.EnteredUrl -> {
                _state.value = _state.value.copy(url = event.value)
            }

            PasswordEvent.SavePassword -> {
                savePassword()
            }
        }
    }



    private fun savePassword() {
        val current = _state.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _state.value = current.copy(error = "Username and Password cannot be empty")
            return
        }

        viewModelScope.launch {
            try {
                _state.value = current.copy(isLoading = true)

                useCases.savePassword(
                    Password(
                        id = current.PasswordId,
                        username = current.username,
                        password = current.password,
                        url = current.url
                    )
                )

                _state.value = current.copy(
                    isLoading = false,
                    isSaved = true
                )

            } catch (e: Exception) {
                _state.value = current.copy(
                    isLoading = false,
                    error = e.message ?: "Error saving password"
                )
            }
        }
    }

}