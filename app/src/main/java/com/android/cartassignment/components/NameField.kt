package com.android.cartassignment.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.cartassignment.R

@Composable
fun NameField(
    name: TextFieldValue,
    onNameValueChange: (newValue: TextFieldValue) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        value = name,
        onValueChange = { newValue ->
            onNameValueChange(newValue)
        },
        label = {
            Text(
                text = stringResource(
                    id = R.string.nameLabel
                )
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(22.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.yellow_700),
            unfocusedBorderColor = colorResource(id = R.color.yellow_700),
            cursorColor = colorResource(id = R.color.yellow_800),
            focusedLabelColor = colorResource(id = R.color.yellow_800),
        )
    )
}