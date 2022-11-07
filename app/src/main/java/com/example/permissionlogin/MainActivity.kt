package com.example.permissionlogin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.permissionlogin.ui.theme.PermissionLoginTheme
import com.google.accompanist.permissions.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

class MainActivity : ComponentActivity() {

    private val permissionViewModel: PermissionViewModel by viewModels()

    private val neededPermissions = listOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.READ_PHONE_NUMBERS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS
    )

    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionLoginTheme(darkTheme = true) {

                val permissionsState = rememberMultiplePermissionsState(
                    permissions = neededPermissions
                )

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_START) {
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )
                PermissionView(permissionsState, permissionViewModel)

            }
        }
    }


}

@ExperimentalPermissionsApi
@Composable
private fun PermissionView(permissionsState: MultiplePermissionsState, permissionViewModel: PermissionViewModel) {
    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (permissionsState.allPermissionsGranted) {
                LoginScreen(permissionViewModel = permissionViewModel)
            } else {
                permissionsState.permissions.forEach { perm ->
                    when {
                        perm.status.shouldShowRationale -> {
                            AlertDialog(
                                onDismissRequest = {},
                                title = {
                                    Text(
                                        text = "Permission Request",
                                        style = TextStyle(
                                            fontSize = MaterialTheme.typography.h6.fontSize,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                },
                                text = {
                                    Text(
                                        text = perm.toString() + " permission is needed" +
                                                "to access the camera"
                                    )
                                },
                                confirmButton = {
                                    Button(onClick = { perm.launchPermissionRequest() }) {
                                        Text("Give Permission")
                                    }
                                }
                            )
                        }
                        perm.isPermanentlyDenied() -> {
                            PermissionDeniedButton(LocalContext.current, perm)
                        }
                    }
                }

            }
        }
    }
}

@ExperimentalPermissionsApi
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !this.status.shouldShowRationale && !this.status.isGranted
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionDeniedButton(current: Context, permission: PermissionState) {
    Button(
        onClick = {
            startActivity(
                current,
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", "com.example.permissionlogin", null)
                ), null
            )
        }
    ) {
        Text(
            permission.toString() + " permission was permanently denied. You can enable it in the app settings."
        )
    }
}

fun login(context: Context, permissionViewModel: PermissionViewModel) {
    permissionViewModel.handleEvent(PermissionEvent.CheckIfHasCall(context))
    permissionViewModel.handleEvent(PermissionEvent.CheckIfContactExist(context))
    permissionViewModel.handleEvent(PermissionEvent.CheckIfHasMsg(context))
    permissionViewModel.handleEvent(PermissionEvent.CheckIfPhoneLyingDown(context))
    permissionViewModel.handleEvent(PermissionEvent.CheckIfBluetoothOn(context))
    permissionViewModel.handleEvent(PermissionEvent.CheckIfDndOn(context))
    permissionViewModel.handleEvent(PermissionEvent.CheckIfFlashOn(context))
    permissionViewModel.handleEvent(PermissionEvent.CheckIfAirplaneModeOn(context))
    permissionViewModel.handleEvent(PermissionEvent.CheckIfBatteryIsCharging(context))
    permissionViewModel.handleEvent(PermissionEvent.LoginClicked(permissionViewModel.passwordTyped))
}

@Composable
fun LoginView(permissionViewModel: PermissionViewModel) {

    if (permissionViewModel.isError) {
        //todo dialogs for errors
        //todo permission
    } else {
        LoginScreen(permissionViewModel = permissionViewModel)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PermissionLoginTheme {
    }
}