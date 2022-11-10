package com.example.permissionlogin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    permissionViewModel: PermissionViewModel
) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(20.dp)) {
        Box(contentAlignment = Alignment.TopCenter)
        {
            Text(
                text = stringResource(id = R.string.WELCOME),
                style = TextStyle(
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
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                shadow = Shadow(color = Color.White, offset = Offset.Infinite, blurRadius = 2f),
                textAlign = TextAlign.Center,
            ),
        )


    }
}





