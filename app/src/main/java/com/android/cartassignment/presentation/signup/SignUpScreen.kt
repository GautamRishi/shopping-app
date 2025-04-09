package com.android.cartassignment.presentation.signup

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.cartassignment.R
import com.android.cartassignment.components.LoadingIndicator
import com.android.cartassignment.core.Constants.PREFS_USER_NAME
import com.android.cartassignment.core.Constants.PREFS_USER_TYPE
import com.android.cartassignment.core.SharedPreferencesHelper
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.core.printError
import com.android.cartassignment.domain.model.Response.Failure
import com.android.cartassignment.domain.model.Response.Loading
import com.android.cartassignment.domain.model.Response.Success
import com.android.cartassignment.navigation.Route.ProductList
import com.android.cartassignment.presentation.signup.components.SignUpContent
import com.android.cartassignment.presentation.signup.components.SignUpTopBar

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateAndClear: (String) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var signingUp by remember { mutableStateOf(false) }
    var dialogMessageId by remember { mutableIntStateOf(0) }
    var emailID by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }

    Scaffold(topBar = {
        SignUpTopBar(
            onArrowBackIconClick = navigateBack
        )
    }) { innerPadding ->
        SignUpContent(
            innerPadding = innerPadding,
            onSigningUp = { name, email, password, confirmPassword ->
                userName = name
                emailID = email
                signingUp = false
                dialogMessage = ""
                if (name.length < 3) {
                    dialogMessageId = R.string.invalidName
                    showDialog = true
                } else if (email.length < 5) {
                    dialogMessageId = R.string.invalidEmail
                    showDialog = true
                } else if (password.length < 8) {
                    dialogMessageId = R.string.invalidPassword
                    showDialog = true
                } else if (password != confirmPassword) {
                    dialogMessageId = R.string.passwordMismatch
                    showDialog = true
                } else {
                    viewModel.signUpWithEmailAndPassword(email, password)
                    signingUp = true
                }
            }, signingUp = signingUp, onSignInTextClick = navigateBack
        )
    }

    if (signingUp) {
        when (val signUpResponse = viewModel.signUpResponse) {
            is Loading -> LoadingIndicator()
            is Success -> {
                viewModel.saveUserData(name = userName, email = emailID,
                    onSuccess = { userData ->
                        SharedPreferencesHelper.saveString(
                            context,
                            PREFS_USER_NAME,
                            userData.userName
                        )
                        SharedPreferencesHelper.saveString(
                            context,
                            PREFS_USER_TYPE,
                            userData.userRole
                        )
                        signingUp = false
                        navigateAndClear(ProductList.route)
                    },
                    onFailure = { e ->
                        printError(e)
                        showDialog = true
                        signingUp = false
                        dialogMessage = e.message ?: ""
                    }
                )
            }

            is Failure -> signUpResponse.e.let { e ->
                printError(e)
                showDialog = true
                signingUp = false
                dialogMessage = e.message ?: ""
            }
        }
    }
    if (showDialog) {
        val message = dialogMessage.ifEmpty { stringResource(dialogMessageId) }
        ShowAlertDialog(
            dialogMessage = message,
            onDismiss = {
                showDialog = false
                dialogMessage = ""
            }
        )
    }
}