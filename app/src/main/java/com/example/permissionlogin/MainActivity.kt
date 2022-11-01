package com.example.permissionlogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.permissionlogin.ui.theme.PermissionLoginTheme

class MainActivity : ComponentActivity() {

    private val permissionViewModel: PermissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionLoginTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    LoginView(permissionViewModel)
                }
            }
        }
    }
}

@Composable
fun LoginView(permissionViewModel: PermissionViewModel) {
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

        Spacer(modifier = Modifier.padding(20.dp))


        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            ExtendedFloatingActionButton(
                modifier = Modifier.padding(10.dp),
                onClick = { /* ... */ },
                text = {
                    Text(
                        text = stringResource(id = R.string.bluetooth),
                        style = TextStyle(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    )
                },
                icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_bluetooth), contentDescription = null)
                },
            )

            ExtendedFloatingActionButton(
                modifier = Modifier.padding(10.dp),
                onClick = { /* ... */ },
                text = {
                    Text(
                        text = stringResource(id = R.string.flash),
                        style = TextStyle(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    )
                },
                icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_flashlight_on), contentDescription = null)
                },
            )

        }

        Spacer(modifier = Modifier.padding(20.dp))

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            ExtendedFloatingActionButton(
                modifier = Modifier.padding(10.dp),
                onClick = { /* ... */ },
                text = {
                    Text(
                        text = stringResource(id = R.string.sensors),
                        style = TextStyle(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    )
                },
                icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_sensors), contentDescription = null)
                },
            )

            ExtendedFloatingActionButton(
                modifier = Modifier.padding(10.dp),
                onClick = { /* ... */ },
                text = {
                    Text(
                        text = stringResource(id = R.string.do_not_disturbe),
                        style = TextStyle(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    )
                },
                icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_do_not_disturb_on), contentDescription = null)
                },
            )

        }

        Spacer(modifier = Modifier.padding(20.dp))

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            ExtendedFloatingActionButton(
                modifier = Modifier.padding(10.dp),
                onClick = { /* ... */ },
                text = {
                    Text(
                        text = stringResource(id = R.string.airplane),
                        style = TextStyle(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    )
                },
                icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_airplanemode_active), contentDescription = null)
                },
            )

            ExtendedFloatingActionButton(
                modifier = Modifier.padding(10.dp),
                onClick = { /* ... */ },
                text = {
                    Text(
                        text = stringResource(id = R.string.charger),
                        style = TextStyle(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    )
                },
                icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_charging_station), contentDescription = null)
                },
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PermissionLoginTheme {

    }
}