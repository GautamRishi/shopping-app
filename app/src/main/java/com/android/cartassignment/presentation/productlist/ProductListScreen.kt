package com.android.cartassignment.presentation.productlist

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.cartassignment.components.LoadingIndicator
import com.android.cartassignment.core.Constants.PREFS_USER_NAME
import com.android.cartassignment.core.Constants.PREFS_USER_TYPE
import com.android.cartassignment.core.SharedPreferencesHelper
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.domain.model.Products
import com.android.cartassignment.navigation.Route.ProductCart
import com.android.cartassignment.navigation.Route.SignIn
import com.android.cartassignment.presentation.productlist.components.ProductListContent
import com.android.cartassignment.presentation.productlist.components.ProductListTopBar

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    navigateAndClear: (String) -> Unit,
    navigate: (String) -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    var loading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val userName = SharedPreferencesHelper.getString(context, PREFS_USER_NAME).toString()
    val userType = SharedPreferencesHelper.getString(context, PREFS_USER_TYPE).toString()
    var products by remember { mutableStateOf<List<Products>>(emptyList()) }


    LaunchedEffect(Unit) {
        viewModel.getAuthState(
            navigateToSignInScreen = {
                navigateAndClear(SignIn.route)
            }
        )
        viewModel.getAllProducts(
            onSuccess = { productList ->
                products = productList
                loading = false
            },
            onFailure = { exception ->
                showDialog = true
                dialogMessage = exception.message ?: ""
                loading = false
            }
        )
    }

    Scaffold(
        topBar = {
            ProductListTopBar(
                userName = userName,
                userType = userType,
                navigate = {
                    navigate(ProductCart.route)
                },
                signOut = {
                    viewModel.signOut()
                }
            )
        },
        scaffoldState = scaffoldState
    ) { innerPadding ->
        if (loading) {
            LoadingIndicator()
        } else {
            ProductListContent(
                navigate = navigate,
                innerPadding = innerPadding,
                userType = userType,
                products = products
            )
        }
    }

    if (showDialog) {
        ShowAlertDialog(
            dialogMessage = dialogMessage,
            onDismiss = {
                showDialog = false
                dialogMessage = ""
            }
        )
    }
}