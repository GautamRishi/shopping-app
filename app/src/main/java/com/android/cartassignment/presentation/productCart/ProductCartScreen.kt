package com.android.cartassignment.presentation.productCart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.cartassignment.components.LoadingIndicator
import com.android.cartassignment.core.Constants.PREFS_CART_ID
import com.android.cartassignment.core.SharedPreferencesHelper
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.domain.model.Order
import com.android.cartassignment.presentation.productCart.components.ProductCartContent
import com.android.cartassignment.presentation.productCart.components.ProductCartTopBar

@Composable
fun ProductCartScreen(
    viewModel: ProductCartViewModel = hiltViewModel(),
    navigate: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val cartID = SharedPreferencesHelper.getString(context, PREFS_CART_ID)
    val scaffoldState = rememberScaffoldState()
    var loading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var cartData by remember { mutableStateOf<Order>(Order()) }

    LaunchedEffect(Unit) {
        if (cartID.isNullOrEmpty()) {
            loading = false
        } else {
            viewModel.getCartData(cartID, onSuccess = { cart ->
                cartData = cart
                loading = false
            }, onFailure = { exception ->
                showDialog = true
                dialogMessage = exception.message ?: ""
                loading = false
            })
        }
    }

    Scaffold(
        topBar = {
            ProductCartTopBar(onArrowBackIconClick = navigateBack)
        }, scaffoldState = scaffoldState
    ) { innerPadding ->
        if (loading) {
            LoadingIndicator()
        } else {
            if (cartID.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your cart is empty",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            } else {
                ProductCartContent(
                    navigate = navigate,
                    innerPadding = innerPadding,
                    cartData = cartData
                )
            }
        }
    }

    if (showDialog) {
        ShowAlertDialog(dialogMessage = dialogMessage, onDismiss = {
            showDialog = false
            dialogMessage = ""
        })
    }
}