package com.android.cartassignment.presentation.addupdateproduct.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.cartassignment.R
import com.android.cartassignment.components.ActionIconButton

@Composable
fun AddUpdateProductTopBar(
    title: String,
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = colorResource(id = R.color.white)

                )
            )
        },
        backgroundColor = colorResource(id = R.color.yellow_700),
        navigationIcon = {
            ActionIconButton(
                onActionIconButtonClick = navigateBack,
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                resourceId = R.string.navigate_back,
                tint = colorResource(id = R.color.white)
            )
        }
    )
}