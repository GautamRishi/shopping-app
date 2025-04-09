package com.android.cartassignment.presentation.orderSummary

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.cartassignment.components.LoadingIndicator
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.domain.model.Order
import com.android.cartassignment.navigation.Route.ProductList
import com.android.cartassignment.presentation.orderSummary.components.OrderSummaryContent
import com.android.cartassignment.presentation.orderSummary.components.OrderSummaryTopBar

@Composable
fun OrderSummaryScreen(
    viewModel: OrderSummaryViewModel = hiltViewModel(),
    orderID: String,
    navigateAndClear: (String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    var loading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var orderData by remember { mutableStateOf<Order>(Order()) }

    LaunchedEffect(Unit) {
        viewModel.getOrderData(orderID,
            onSuccess = { order ->
                orderData = order
                loading = false
            }, onFailure = { exception ->
                showDialog = true
                dialogMessage = exception.message ?: ""
                loading = false
            })
    }
    BackHandler {
        navigateAndClear(ProductList.route)
    }
    Scaffold(
        topBar = {
            OrderSummaryTopBar(onArrowBackIconClick = { navigateAndClear(ProductList.route) })
        }, scaffoldState = scaffoldState
    ) { innerPadding ->
        if (loading) {
            LoadingIndicator()
        } else {
            OrderSummaryContent(
                innerPadding = innerPadding,
                orderData = orderData
            )
        }
    }

    if (showDialog) {
        ShowAlertDialog(dialogMessage = dialogMessage, onDismiss = {
            showDialog = false
            dialogMessage = ""
        })
    }
}