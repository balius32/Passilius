package com.balius.passilius.features.password.presentation.home.presentation


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balius.passilius.features.password.domain.useCases.PasswordUseCases
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeViewModel(
    private val useCases: PasswordUseCases
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetPassword -> {
                getPasswords()
            }

            is HomeEvent.Error -> {
            }

        }


    }

    private fun getPasswords() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                useCases.getPasswords().collectLatest { passwords ->
                    _state.value = _state.value.copy(
                        passwords = passwords,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

 /*   private fun getPasswords() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            useCases.getPasswords()
                .collect { passwords ->
                    _state.value = _state.value.copy(
                        passwords = passwords,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }*/
  /*  private fun getPasswords() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val passwords = useCases.getPasswords()
                _state.value = _state.value.copy(
                    passwords = passwords,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }*/
}