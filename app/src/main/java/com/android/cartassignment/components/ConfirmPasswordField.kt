package com.android.cartassignment.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.android.cartassignment.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmPasswordField(
    confirmPassword: TextFieldValue,
    onConfirmPasswordValueChange: (newValue: TextFieldValue) -> Unit
) {
    var confirmPasswordIsVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = confirmPassword,
        onValueChange = { newValue ->
            onConfirmPasswordValueChange(newValue)
        },
        label = {
            Text(
                text = stringResource(
                    id = R.string.confirmPasswordLabel
                )
            )
        },
        singleLine = true,
        visualTransformation = if (confirmPasswordIsVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp),
        shape = RoundedCornerShape(22.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.yellow_700),
            unfocusedBorderColor = colorResource(id = R.color.yellow_700),
            cursorColor = colorResource(id = R.color.yellow_800),
            focusedLabelColor = colorResource(id = R.color.yellow_800),
        ),
        trailingIcon = {
            val icon = if (confirmPasswordIsVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(
                onClick = {
                    confirmPasswordIsVisible = !confirmPasswordIsVisible
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }
    )
}