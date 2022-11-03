package com.example.permissionlogin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Camera
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.permissionlogin.ui.theme.PermissionLoginTheme


class MainActivity : ComponentActivity() {

    private val permissionViewModel: PermissionViewModel by viewModels()

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Do something if permission granted
            if (isGranted) {
                Log.i("DEBUG", "permission granted")
            } else {
                Log.i("DEBUG", "permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionLoginTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    LoginView(permissionViewModel, requestPermission)
                }
            }
        }
    }
}

@Composable
fun askPermission() {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("ExampleScreen", "PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d("ExampleScreen", "PERMISSION DENIED")
        }
    }
    val context = LocalContext.current

    Button(
        onClick = {
            // Check permission
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) -> {
                    // Some works that require permission
                    Log.d("ExampleScreen", "Code requires permission")
                }
                else -> {
                    // Asking for permission
                    launcher.launch(Manifest.permission.CAMERA)
                }
            }
        }
    ) {
        Text(text = "Check and Request Permission")
    }

}


@Composable
fun LoginView(permissionViewModel: PermissionViewModel, requestPermission: ActivityResultLauncher<String>) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(20.dp)) {
        Box(contentAlignment = Alignment.TopCenter)
        {
            Text(
                text = stringResource(id = R.string.welcome),
                style = TextStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    shadow = Shadow(color = Color.White, offset = Offset.Infinite, blurRadius = 2f),
                    textAlign = TextAlign.Center,
                ),
            )
        }

        Spacer(modifier = Modifier.padding(30.dp))

        Text(
            text = permissionViewModel.loginResult.asString(context),
            style = TextStyle(
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                shadow = Shadow(color = Color.White, offset = Offset.Infinite, blurRadius = 2f),
                textAlign = TextAlign.Center,
            ),
        )

        Spacer(modifier = Modifier.padding(10.dp))

        TextField(
            value = permissionViewModel.passwordTyped,
            label = {
                Text(
                    text = stringResource(id = R.string.password),
                    style = TextStyle(
                        color = MaterialTheme.colors.primary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                KeyboardCapitalization.None,
                false,
                KeyboardType.Text,
                ImeAction.None
            ),
            onValueChange = { newText ->
                permissionViewModel.handleEvent(PermissionEvent.UpdatePasswordTyped(newText))
            }
        )

        Spacer(modifier = Modifier.padding(20.dp))

        ExtendedFloatingActionButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                permissionViewModel.handleEvent(PermissionEvent.CheckIfHasMsg(context))
                permissionViewModel.handleEvent(PermissionEvent.CheckIfPhoneLyingDown(context))
                permissionViewModel.handleEvent(PermissionEvent.CheckIfBluetoothOn(context))
                permissionViewModel.handleEvent(PermissionEvent.CheckIfDndOn(context))
                permissionViewModel.handleEvent(PermissionEvent.CheckIfFlashOn(context))
                permissionViewModel.handleEvent(PermissionEvent.CheckIfAirplaneModeOn(context))
                permissionViewModel.handleEvent(PermissionEvent.CheckIfBatteryIsCharging(context))
                permissionViewModel.handleEvent(PermissionEvent.LoginClicked(permissionViewModel.passwordTyped))
            },
            text = {
                Text(
                    text = stringResource(id = R.string.login),
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                    )
                )
            },
            icon = { Icon(painter = painterResource(id = R.drawable.ic_login), contentDescription = null) },
            backgroundColor = MaterialTheme.colors.primary,
            shape = MaterialTheme.shapes.small
        )

        //todo permission
        requestPermission.launch(Manifest.permission.CAMERA)
        requestPermission.launch(Manifest.permission.BLUETOOTH_ADMIN)

        //todo dialogs for errors

//        Spacer(modifier = Modifier.padding(20.dp))
//
//        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
//            ExtendedFloatingActionButton(
//                modifier = Modifier.padding(10.dp),
//                onClick = { /* ... */ },
//                text = {
//                    Text(
//                        text = stringResource(id = R.string.bluetooth),
//                        style = TextStyle(
//                            fontSize = 12.sp,
//                            textAlign = TextAlign.Center,
//                        )
//                    )
//                },
//                icon = {
//                    Icon(painter = painterResource(id = R.drawable.ic_bluetooth), contentDescription = null)
//                },
//            )
//
//            ExtendedFloatingActionButton(
//                modifier = Modifier.padding(10.dp),
//                onClick = { /* ... */ },
//                text = {
//                    Text(
//                        text = stringResource(id = R.string.flash),
//                        style = TextStyle(
//                            fontSize = 12.sp,
//                            textAlign = TextAlign.Center,
//                        )
//                    )
//                },
//                icon = {
//                    Icon(painter = painterResource(id = R.drawable.ic_flashlight_on), contentDescription = null)
//                },
//            )
//
//        }
//
//        Spacer(modifier = Modifier.padding(20.dp))
//
//        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
//            ExtendedFloatingActionButton(
//                modifier = Modifier.padding(10.dp),
//                onClick = { /* ... */ },
//                text = {
//                    Text(
//                        text = stringResource(id = R.string.sensors),
//                        style = TextStyle(
//                            fontSize = 12.sp,
//                            textAlign = TextAlign.Center,
//                        )
//                    )
//                },
//                icon = {
//                    Icon(painter = painterResource(id = R.drawable.ic_sensors), contentDescription = null)
//                },
//            )
//
//            ExtendedFloatingActionButton(
//                modifier = Modifier.padding(10.dp),
//                onClick = { /* ... */ },
//                text = {
//                    Text(
//                        text = stringResource(id = R.string.do_not_disturb),
//                        style = TextStyle(
//                            fontSize = 12.sp,
//                            textAlign = TextAlign.Center,
//                        )
//                    )
//                },
//                icon = {
//                    Icon(painter = painterResource(id = R.drawable.ic_do_not_disturb_on), contentDescription = null)
//                },
//            )
//
//        }
//
//        Spacer(modifier = Modifier.padding(20.dp))



//        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
//            ExtendedFloatingActionButton(
//                modifier = Modifier.padding(10.dp),
//                onClick = { /* ... */ },
//                text = {
//                    Text(
//                        text = stringResource(id = R.string.airplane),
//                        style = TextStyle(
//                            fontSize = 12.sp,
//                            textAlign = TextAlign.Center,
//                        )
//                    )
//                },
//                icon = {
//                    Icon(painter = painterResource(id = R.drawable.ic_airplanemode_active), contentDescription = null)
//                },
//            )
//
//
//
//            ExtendedFloatingActionButton(
//                modifier = Modifier.padding(10.dp),
//                onClick = {
//                        var isCharging = checkIfBatteryIsCharging(context)
//                },
//                text = {
//                    Text(
//                        text = stringResource(id = R.string.charger),
//                        style = TextStyle(
//                            fontSize = 12.sp,
//                            textAlign = TextAlign.Center,
//                        )
//                    )
//                },
//                icon = {
//                    Icon(painter = painterResource(id = R.drawable.ic_charging_station), contentDescription = null)
//                },
//            )
//
//
//        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PermissionLoginTheme {

    }
}