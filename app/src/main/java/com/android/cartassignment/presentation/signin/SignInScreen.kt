package com.android.cartassignment.presentation.signin

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.android.cartassignment.domain.model.Response.Failure
import com.android.cartassignment.domain.model.Response.Loading
import com.android.cartassignment.domain.model.Response.Success
import com.android.cartassignment.navigation.Route.ProductList
import com.android.cartassignment.navigation.Route.SignUp
import com.android.cartassignment.presentation.signin.components.SignInContent
import com.android.cartassignment.presentation.signin.components.SignInTopBar

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigate: (String) -> Unit,
    navigateAndClear: (String) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var signingIn by remember { mutableStateOf(false) }
    var dialogMessageId by remember { mutableIntStateOf(0) }
    var emailID by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SignInTopBar()
        }
    ) { innerPadding ->
        SignInContent(
            innerPadding = innerPadding,
            onSigningIn = { email, password ->
                signingIn = false
                dialogMessage = ""
                if (email.length < 5) {
                    dialogMessageId = R.string.invalidEmail
                    showDialog = true
                } else if (password.length < 8) {
                    dialogMessageId = R.string.invalidPassword
                    showDialog = true
                } else {
                    emailID = email
                    viewModel.signInWithEmailAndPassword(email, password)
                    signingIn = true
                }
            },
            signingIn = signingIn,
            onSignUpTextClick = {
                navigate(SignUp.route)
            }
        )
    }

    if (signingIn) {
        val signInResponse by viewModel.signInResponse.collectAsState()
        when (signInResponse) {
            is Loading -> LoadingIndicator()
            is Success -> {
                viewModel.getUserData(email = emailID,
                    onSuccess = { user ->
                        signingIn = false
                        SharedPreferencesHelper.saveString(context, PREFS_USER_NAME, user.userName)
                        SharedPreferencesHelper.saveString(context, PREFS_USER_TYPE, user.userRole)
                        navigateAndClear(ProductList.route)
                    },
                    onFailure = { e ->
                        signingIn = false
                        showDialog = true
                        dialogMessage = e.message ?: ""
                    }
                )
            }

            is Failure -> (signInResponse as Failure).e.let { e ->
                signingIn = false
                showDialog = true
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