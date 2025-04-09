package com.android.cartassignment.presentation.signup.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.cartassignment.R
import com.android.cartassignment.components.ActionButton
import com.android.cartassignment.components.ActionText
import com.android.cartassignment.components.ConfirmPasswordField
import com.android.cartassignment.components.EmailField
import com.android.cartassignment.components.NameField
import com.android.cartassignment.components.PasswordField
import com.android.cartassignment.core.Constants.EMPTY_STRING

@Composable
fun SignUpContent(
    innerPadding: PaddingValues,
    onSigningUp: (String, String, String, String) -> Unit,
    signingUp: Boolean,
    onSignInTextClick: () -> Unit
) {
    var name by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(EMPTY_STRING)) }
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(EMPTY_STRING)) }
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(EMPTY_STRING)) }
    var confirmPassword by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(EMPTY_STRING)) }
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameField(
            name = name,
            onNameValueChange = { newName ->
                name = newName
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )
        EmailField(
            email = email,
            onEmailValueChange = { newValue ->
                email = newValue
            }
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        PasswordField(
            password = password,
            onPasswordValueChange = { newValue ->
                password = newValue
            }
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        ConfirmPasswordField(
            confirmPassword = confirmPassword,
            onConfirmPasswordValueChange = { newConfirmPassword ->
                confirmPassword = newConfirmPassword
            }
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        ActionButton(
            onActionButtonClick = {
                onSigningUp(
                    name.text,
                    email.text,
                    password.text,
                    confirmPassword.text
                )
                keyboard?.hide()
            },
            enabled = !signingUp,
            resourceId = R.string.signUp
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        ActionText(
            onActionTextClick = {
                onSignInTextClick()
                name = TextFieldValue(EMPTY_STRING)
                email = TextFieldValue(EMPTY_STRING)
                password = TextFieldValue(EMPTY_STRING)
                confirmPassword = TextFieldValue(EMPTY_STRING)
            },
            resourceId = R.string.signIn
        )
    }
}