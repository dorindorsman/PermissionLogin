package com.example.permissionlogin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PermissionViewModel : ViewModel() {

    private val rightPassword = "Dd3012"
    var passwordTyped by mutableStateOf("")
    var loginResult by mutableStateOf(UiText.StringResource(R.string.enter_password))


    fun handleEvent(event: PermissionEvent) {
        when (event) {
            is PermissionEvent.UpdatePasswordTyped -> handleUpdatePasswordTyped(event.p)
            is PermissionEvent.LoginClicked -> handleLoginClicked(event.p)
        }
    }


    private fun handleUpdatePasswordTyped(p: String) {
        passwordTyped = p
    }

    private fun handleLoginClicked(p: String) {
        loginResult = if (p.isNotEmpty() && p == rightPassword) {
            UiText.StringResource(R.string.success)
        } else {
            UiText.StringResource(R.string.fail)
        }
    }


}
