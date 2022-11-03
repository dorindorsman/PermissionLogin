package com.example.permissionlogin

import android.bluetooth.BluetoothManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraManager.TorchCallback
import android.os.BatteryManager
import android.provider.Settings
import android.provider.Telephony
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.database.getStringOrNull
import androidx.lifecycle.ViewModel


class PermissionViewModel : ViewModel() {

    private val rightPassword = "Dd3012"
    var passwordTyped by mutableStateOf("")
    private var isCharging by mutableStateOf(false)
    private var isAirplaneModeOn by mutableStateOf(false)
    private var isFlashOn by mutableStateOf(false)
    private var isDndOn by mutableStateOf(false)
    private var isBluetoothOn by mutableStateOf(false)
    private var isLyingDown by mutableStateOf(false)
    private var hasSms by mutableStateOf(false)
    private var hasMms by mutableStateOf(false)
    private var hasMsg by mutableStateOf(false)
    var loginResult by mutableStateOf(UiText.StringResource(R.string.enter_password))



    fun handleEvent(event: PermissionEvent) {
        when (event) {
            is PermissionEvent.UpdatePasswordTyped -> handleUpdatePasswordTyped(event.password)
            is PermissionEvent.LoginClicked -> handleLoginClicked(event.password)
            is PermissionEvent.CheckIfBatteryIsCharging -> handleCheckIfBatteryIsCharging(event.context)
            is PermissionEvent.CheckIfAirplaneModeOn -> handleCheckIfAirplaneModeOn(event.context)
            is PermissionEvent.CheckIfFlashOn -> handleCheckIfFlashOn(event.context)
            is PermissionEvent.CheckIfDndOn-> handleCheckIfDndOn(event.context)
            is PermissionEvent.CheckIfBluetoothOn-> handleCheckIfBluetoothOn(event.context)
            is PermissionEvent.CheckIfPhoneLyingDown-> handleCheckIfPhoneLyingDown(event.context)
            is PermissionEvent.CheckIfHasMsg-> handleCheckIfHasMsg(event.context)
        }
    }

    private fun handleUpdatePasswordTyped(password: String) {
        passwordTyped = password
    }

    private fun handleLoginClicked(password: String) {
        if (!isCharging) {
            loginResult = UiText.StringResource(R.string.fail)
            Log.d("dorin","isCharging" )
            return
        }

        if (!isAirplaneModeOn) {
            loginResult = UiText.StringResource(R.string.fail)
            Log.d("dorin","isAirplaneModeOn" )
            return
        }

        if (!isFlashOn) {
            loginResult = UiText.StringResource(R.string.fail)
            Log.d("dorin","isFlashOn" + isFlashOn )
            return
        }

        if (!isDndOn) {
            loginResult = UiText.StringResource(R.string.fail)
            Log.d("dorin","isDndOn" )
            return
        }

        if (!isBluetoothOn) {
            loginResult = UiText.StringResource(R.string.fail)
            Log.d("dorin","isBluetoothOn" )
            return
        }

        if (!isLyingDown) {
            loginResult = UiText.StringResource(R.string.fail)
            Log.d("dorin","isLyingDown" )
            return
        }

        if (!hasMsg) {
            loginResult = UiText.StringResource(R.string.fail)
            Log.d("dorin","hasMsg" )
            return
        }

        if (!handlePasswordCorrectnessCheck(password)) {
            return
        }

        loginResult = UiText.StringResource(R.string.success)
    }

    private fun handlePasswordCorrectnessCheck(password: String): Boolean {
        if (password.isNotEmpty() && password == rightPassword) {
            loginResult = UiText.StringResource(R.string.correct_password)
            return true
        }
        loginResult = UiText.StringResource(R.string.incorrect_password)
        return false
    }

    private fun handleCheckIfBatteryIsCharging(context: Context) {
        val myBatteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        isCharging = myBatteryManager.isCharging
    }

    private fun handleCheckIfAirplaneModeOn(context: Context)  {
        isAirplaneModeOn = Settings.System.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON) != 0
    }

    private fun handleCheckIfFlashOn(context: Context)  {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager.registerTorchCallback(torchCallback, null)
    }

    private val torchCallback: TorchCallback = object : TorchCallback() {
        override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
            super.onTorchModeChanged(cameraId, enabled)
            isFlashOn = enabled
        }
    }

    private fun handleCheckIfDndOn(context: Context) {
        /**
        0 - If DnD is off.
        1 - If DnD is on - Priority Only
        2 - If DnD is on - Total Silence
        3 - If DnD is on - Alarms Only
         * */
        isDndOn = Settings.Global.getInt(context.contentResolver, "zen_mode") != 0
    }


    private fun handleCheckIfBluetoothOn(context: Context) {
        val myBluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        isBluetoothOn = myBluetoothAdapter.isEnabled
    }

    private fun handleCheckIfPhoneLyingDown(context: Context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private val accSensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val y = event.values[1]
            if(-1<y && y<1){
                isLyingDown=true
                return
            }
            isLyingDown=false
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun handleCheckIfHasMsg(context: Context) {
        checkIfHasMsg(context)
        if(hasMms || hasSms){
            hasMsg = true
            return
        }
        hasMsg = false
    }

    private fun checkIfHasMsg(context: Context) {
        val smsCursor = context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER)
        smsCursor?.let {
            if (it.moveToFirst()) {
                do {
                    if(it.getString(it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)).equals(UiText.StringResource(R.string.phone1)) || it.getString(it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)).equals(UiText.StringResource(R.string.phone2))){
                        hasSms = true
                        return
                    }
                } while (it.moveToNext())
            }
            it.close()
            hasSms = false
        }

        val mmsCursor = context.contentResolver.query(Telephony.Mms.CONTENT_URI, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER)
        mmsCursor?.let {
            if (it.moveToFirst()) {
                do {
                    it.getStringOrNull(it.getColumnIndexOrThrow(Telephony.Mms.CREATOR)).toString()
                    if(it.getStringOrNull(it.getColumnIndexOrThrow(Telephony.Mms.CREATOR)).toString().equals(UiText.StringResource(R.string.phone1)) || it.getStringOrNull(it.getColumnIndexOrThrow(Telephony.Mms.CREATOR)).toString().equals(UiText.StringResource(R.string.phone2))){
                        hasMms = true
                        return
                    }
                } while (it.moveToNext())
            }
            it.close()
            hasMms = false
        }
    }


    private fun handleCheckIfContactExist(context: Context) {
        //todo
    }


    private fun handleCheckIfCallExist(context: Context) {
        //todo
    }

}
