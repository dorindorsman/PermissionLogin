package com.example.permissionlogin

sealed class PermissionEvent {
    class UpdatePasswordTyped(val p:String) : PermissionEvent()
    class LoginClicked(val p:String) : PermissionEvent()
}