package com.android.cartassignment.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.cartassignment.R

@Composable
fun ActionButton(
    onActionButtonClick: () -> Unit,
    enabled: Boolean,
    resourceId: Int
) {
    Button(
        onClick = onActionButtonClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.yellow_900)
        ),
        modifier = Modifier
            .width(150.dp)
            .height(48.dp)
    ) {
        Text(
            text = stringResource(
                id = resourceId
            ),
            fontSize = 18.sp,
            color = colorResource(id = R.color.white)
        )
    }
}