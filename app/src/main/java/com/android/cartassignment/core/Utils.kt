package com.android.cartassignment.core

import android.util.Log
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.android.cartassignment.domain.model.Response
import java.util.UUID

const val TAG = "Tag"

fun printError(e: Exception) = Log.e(TAG, "${e.message}")

@Composable
fun ShowAlertDialog(
    dialogMessage: String,
    onDismiss: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                onDismiss()
            },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    onDismiss()
                }) {
                    Text("Ok")
                }
            }
        )
    }
}

fun generateID(prefix: String): String {
    return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 4)
}

suspend fun <T> launchCatching(block: suspend () -> T) = try {
    Response.Success(block())
} catch (e: Exception) {
    Response.Failure(e)
}