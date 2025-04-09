package com.android.cartassignment.presentation.signin.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.cartassignment.R
import com.android.cartassignment.components.ActionButton
import com.android.cartassignment.components.ActionText
import com.android.cartassignment.components.EmailFieldSignIn
import com.android.cartassignment.components.PasswordField
import com.android.cartassignment.core.Constants.EMPTY_STRING

@Composable
fun SignInContent(
    innerPadding: PaddingValues,
    onSigningIn: (String, String) -> Unit,
    signingIn: Boolean,
    onSignUpTextClick: () -> Unit
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(EMPTY_STRING)) }
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(EMPTY_STRING)) }
    val keyboard = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                innerPadding.calculateTopPadding(),
                26.dp,
                innerPadding.calculateTopPadding(),
                innerPadding.calculateTopPadding()
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.SignInMessage), style = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(id = R.color.yellow_800)

            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailFieldSignIn(email = email, onEmailValueChange = { newEmail ->
            email = newEmail
        })

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        PasswordField(password = password, onPasswordValueChange = { newPassword ->
            password = newPassword
        })
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        ActionButton(
            onActionButtonClick = {
                onSigningIn(email.text, password.text)
                keyboard?.hide()
            }, enabled = !signingIn, resourceId = R.string.sign_in_button
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )
        Row {
            ActionText(
                onActionTextClick = { },
                resourceId = R.string.forgot_password
            )
            Text(
                modifier = Modifier.padding(
                    start = 4.dp,
                    end = 4.dp
                ),
                text = " | ",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            ActionText(
                onActionTextClick = {
                    onSignUpTextClick()
                    email = TextFieldValue(EMPTY_STRING)
                    password = TextFieldValue(EMPTY_STRING)
                },
                resourceId = R.string.createAccount
            )
        }
    }
}