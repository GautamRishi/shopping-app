package com.android.cartassignment.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.android.cartassignment.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
            .clickable(enabled = false){},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier,
            color = colorResource(R.color.yellow_900)
        )
    }
}