package com.android.cartassignment.presentation.productdetails

import android.util.Log
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.cartassignment.components.LoadingIndicator
import com.android.cartassignment.core.Constants.PREFS_PRODUCT_ID
import com.android.cartassignment.core.Constants.PREFS_PRODUCT_NAME
import com.android.cartassignment.core.Constants.PRODUCT_IMAGE_TABLE
import com.android.cartassignment.core.SharedPreferencesHelper
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.domain.model.Products
import com.android.cartassignment.navigation.Route.ProductList
import com.android.cartassignment.presentation.productdetails.components.ProductDetailsContent
import com.android.cartassignment.presentation.productdetails.components.ProductDetailsTopBar
import kotlinx.coroutines.launch


@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    navigate: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val productId = SharedPreferencesHelper.getString(context, PREFS_PRODUCT_ID)
    val productsName = SharedPreferencesHelper.getString(context, PREFS_PRODUCT_NAME)
    var loading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var product by remember { mutableStateOf<Products>(Products()) }
    var imageUrl by remember { mutableStateOf<String>("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getProductDetails(productId.toString(),
            onSuccess = { fetchedProduct ->
                product = fetchedProduct
                coroutineScope.launch {
                    imageUrl = viewModel.getImageUrl("${PRODUCT_IMAGE_TABLE}${product.imageName}")
                }
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
            ProductDetailsTopBar(
                productName = productsName.toString(),
                onArrowBackIconClick = navigateBack
            )
        },
        scaffoldState = scaffoldState
    ) { innerPadding ->
        if (loading && imageUrl.isEmpty()) {
            LoadingIndicator()
        } else {
            ProductDetailsContent(
                innerPadding = innerPadding,
                product = product,
                imageUrl = imageUrl,
                navigate = navigate
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