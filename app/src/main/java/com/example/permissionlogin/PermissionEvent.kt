package com.example.permissionlogin

import android.content.Context

sealed class PermissionEvent {
    class UpdatePasswordTyped(val password:String) : PermissionEvent()
    class LoginClicked(val password:String) : PermissionEvent()
    class CheckIfBatteryIsCharging(val context:Context) : PermissionEvent()
    class CheckIfAirplaneModeOn(val context:Context) : PermissionEvent()
    class CheckIfFlashOn (val context:Context) : PermissionEvent()
    class CheckIfDndOn(val context: Context) : PermissionEvent()
    class CheckIfBluetoothOn(val context: Context) : PermissionEvent()
    class CheckIfPhoneLyingDown(val context: Context) : PermissionEvent()
    class CheckIfHasMsg(val context: Context) : PermissionEvent()
    class CheckIfContactExist(val context: Context) : PermissionEvent()
    class CheckIfHasCall(val context: Context) : PermissionEvent()

}