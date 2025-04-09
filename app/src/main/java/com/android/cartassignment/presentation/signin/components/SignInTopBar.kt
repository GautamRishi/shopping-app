package com.android.cartassignment.presentation.signin.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.android.cartassignment.R

@Composable
fun SignInTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.sign_in_screen_title),
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = colorResource(id = R.color.white)

                )
            )
        },
        backgroundColor = colorResource(id = R.color.yellow_700)
    )
}