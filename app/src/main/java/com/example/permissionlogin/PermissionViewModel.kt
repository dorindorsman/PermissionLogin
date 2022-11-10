package com.example.permissionlogin

import android.bluetooth.BluetoothManager
import android.content.Context
import android.database.Cursor
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraManager.TorchCallback
import android.net.Uri
import android.os.BatteryManager
import android.provider.CallLog
import android.provider.ContactsContract.PhoneLookup
import android.provider.Settings
import android.provider.Telephony
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.database.getStringOrNull
import androidx.lifecycle.ViewModel
import java.lang.Long
import java.sql.Date
import java.time.LocalDate
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.arrayOf
import kotlin.let


class PermissionViewModel : ViewModel() {

    private val rightPassword = "Dd3012"
    var passwordTyped by mutableStateOf("")
    private var isCharging by mutableStateOf(false)
    private var isAirplaneModeOn by mutableStateOf(false)
    private var isFlashOn by mutableStateOf(false)
    private var isDndOn by mutableStateOf(false)
    private var isBluetoothOn by mutableStateOf(false)
    private var isLyingDown by mutableStateOf(false)
    private var hasMsg by mutableStateOf(false)
    private var hasCall by mutableStateOf(false)
    private var contactExist by mutableStateOf(false)
    var loginResult by mutableStateOf(UiText.StringResource(R.string.ENTER_PASSWORD))
    var isError by mutableStateOf(false)


    fun handleEvent(event: PermissionEvent) {
        when (event) {
            is PermissionEvent.UpdatePasswordTyped -> handleUpdatePasswordTyped(event.password)
            is PermissionEvent.LoginClicked -> handleLoginClicked(event.password)
            is PermissionEvent.CheckIfBatteryIsCharging -> handleCheckIfBatteryIsCharging(event.context)
            is PermissionEvent.CheckIfAirplaneModeOn -> handleCheckIfAirplaneModeOn(event.context)
            is PermissionEvent.CheckIfFlashOn -> handleCheckIfFlashOn(event.context)
            is PermissionEvent.CheckIfDndOn -> handleCheckIfDndOn(event.context)
            is PermissionEvent.CheckIfBluetoothOn -> handleCheckIfBluetoothOn(event.context)
            is PermissionEvent.CheckIfPhoneLyingDown -> handleCheckIfPhoneLyingDown(event.context)
            is PermissionEvent.CheckIfHasMsg -> handleCheckIfHasMsg(event.context)
            is PermissionEvent.CheckIfContactExist -> handleCheckIfContactExist(event.context)
            is PermissionEvent.CheckIfHasCall -> handleCheckIfHasCall(event.context)
        }
    }

    private fun handleUpdatePasswordTyped(password: String) {
        passwordTyped = password
    }

    private fun handleLoginClicked(password: String) {
        if (!isCharging) {
            loginResult = UiText.StringResource(R.string.CHARGER)
            isError = true
            Log.d("dorin", "isCharging")
            return
        }

        if (!isAirplaneModeOn) {
            loginResult = UiText.StringResource(R.string.AIRPLANE_MODE)
            isError = true
            Log.d("dorin", "isAirplaneModeOn")
            return
        }

        if (!isFlashOn) {
            loginResult = UiText.StringResource(R.string.FLASH)
            isError = true
            Log.d("dorin", "isFlashOn" + isFlashOn)
            return
        }

        if (!isDndOn) {
            loginResult = UiText.StringResource(R.string.DND)
            isError = true
            Log.d("dorin", "isDndOn")
            return
        }

        if (!isBluetoothOn) {
            loginResult = UiText.StringResource(R.string.BLUETOOTH)
            isError = true
            Log.d("dorin", "isBluetoothOn")
            return
        }

        if (!isLyingDown) {
            loginResult = UiText.StringResource(R.string.SENSORS)
            isError = true
            Log.d("dorin", "isLyingDown")
            return
        }

        if (!hasMsg) {
            loginResult = UiText.StringResource(R.string.MSG)
            isError = true
            Log.d("dorin", "hasMsg")
            return
        }

        if (!contactExist) {
            loginResult = UiText.StringResource(R.string.CONTACT)
            isError = true
            Log.d("dorin", "contactExist")
            return
        }

        if (!hasCall) {
            loginResult = UiText.StringResource(R.string.CALL)
            isError = true
            Log.d("dorin", "hasCall")
            return
        }

        if (!handlePasswordCorrectnessCheck(password)) {
            isError = true
            return
        }

        isError = false
        loginResult = UiText.StringResource(R.string.SUCCESS)
    }

    private fun handlePasswordCorrectnessCheck(password: String): Boolean {
        if (password.isNotEmpty() && password == rightPassword) {
            loginResult = UiText.StringResource(R.string.correct_password)
            return true
        }
        loginResult = UiText.StringResource(R.string.INCORRECT_PASSWORD)
        return false
    }

    private fun handleCheckIfBatteryIsCharging(context: Context) {
        val myBatteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        isCharging = myBatteryManager.isCharging
    }

    private fun handleCheckIfAirplaneModeOn(context: Context) {
        isAirplaneModeOn = Settings.System.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON) != 0
    }

    private fun handleCheckIfFlashOn(context: Context) {
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
            if (-1 < y && y < 1) {
                isLyingDown = true
                return
            }
            isLyingDown = false
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun handleCheckIfHasMsg(context: Context) {
        if (checkIfHasMsg(context)) {
            hasMsg = true
            return
        }
        hasMsg = false
    }

    private fun checkIfHasMsg(context: Context): Boolean {
        val smsCursor = context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER)
        smsCursor?.let {
            if (it.moveToFirst()) {
                do {
                    if (it.getStringOrNull(it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                            .equals(UiText.StringResource(R.string.PHONE1).asString(context)) ||
                        it.getStringOrNull(it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                            .equals(UiText.StringResource(R.string.PHONE2).asString(context))
                    ) {
                        return true
                    }
                } while (it.moveToNext())
            }
            it.close()
        }

        val mmsCursor = context.contentResolver.query(Telephony.Mms.CONTENT_URI, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER)
        mmsCursor?.let {
            if (it.moveToFirst()) {
                do {
                    if (it.getStringOrNull(it.getColumnIndexOrThrow(Telephony.Mms.CREATOR)).equals(
                            UiText.StringResource(R.string.PHONE1)
                                .asString(context)
                        ) || it.getStringOrNull(it.getColumnIndexOrThrow(Telephony.Mms.CREATOR)).equals(
                            UiText.StringResource(R.string.PHONE2).asString(context)
                        )
                    ) {
                        return true
                    }
                } while (it.moveToNext())
            }
            it.close()
        }
        return false
    }


    private fun handleCheckIfContactExist(context: Context) {
        val lookupUri1 =
            Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(UiText.StringResource(R.string.PHONE1).asString(context)))
        val lookupUri2 =
            Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(UiText.StringResource(R.string.PHONE2).asString(context)))
        val phoneNumberProjection = arrayOf(PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME)

        val cursor1 = context.contentResolver.query(lookupUri1, phoneNumberProjection, null, null, null);
        cursor1?.let {
            cursor1.use { cursor1 ->
                if (cursor1.moveToFirst()) {
                    contactExist = true
                    return
                }
            }
        }

        val cursor2 = context.contentResolver.query(lookupUri2, phoneNumberProjection, null, null, null);
        cursor2?.let {
            cursor2.use { cursor2 ->
                if (cursor2.moveToFirst()) {
                    contactExist = true
                    return
                }
            }
        }
        contactExist = false
    }


    private fun handleCheckIfHasCall(context: Context) {
        val cursor: Cursor? = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    val callDate: String = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))
                    val phoneNumber: String = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val callDateFormat = Date(Long.valueOf(callDate))
                    val currentDateFormat = LocalDate.now().toString()
                    var direction: String? = null
                    when (cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE)).toInt()) {
                        CallLog.Calls.INCOMING_TYPE -> direction = "INGOING"
                        CallLog.Calls.OUTGOING_TYPE -> direction = "OUTGOING"
                        CallLog.Calls.MISSED_TYPE -> direction = "MISSED"
                    }
                    if (phoneNumber.equals(
                            UiText.StringResource(R.string.PHONE1).asString(context)
                        ) || phoneNumber.equals(UiText.StringResource(R.string.PHONE2).asString(context))
                    ) {
                        if (direction.equals("INGOING") && currentDateFormat.equals(callDateFormat.toString())) {
                            hasCall = true
                            return
                        }
                    }
                }
            }
            cursor.close()
        }

        hasCall = false
    }

}
