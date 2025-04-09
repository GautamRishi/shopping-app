package com.android.cartassignment.presentation.addupdateproduct

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.cartassignment.R
import com.android.cartassignment.components.LoadingIndicator
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.core.printError
import com.android.cartassignment.presentation.addupdateproduct.components.AddUpdateProductContent
import com.android.cartassignment.presentation.addupdateproduct.components.AddUpdateProductTopBar

@Composable
fun AddUpdateProductScreen(
    viewModel: AddUpdateProductViewModel = hiltViewModel(),
    productID: String,
    productName: String,
    productDetails: String,
    imageName: String,
    imageUrl: String,
    productQuantity: Int,
    price: Int,
    productVisibility: Boolean,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var savedShowDialog by remember { mutableStateOf(false) }
    var dialogMessageId by remember { mutableIntStateOf(0) }
    var dialogMessage by remember { mutableStateOf("") }
    var savedDialogMessage by remember { mutableStateOf("") }

    Scaffold(topBar = {
        AddUpdateProductTopBar(
            title = if (productID.isEmpty()) stringResource(id = R.string.add_product_screen_title) else
                stringResource(id = R.string.update_product_screen_title),
            navigateBack = navigateBack
        )
    }) { innerPadding ->
        AddUpdateProductContent(
            productID = productID,
            productName = productName,
            productDetails = productDetails,
            imageUrl = imageUrl,
            productQuantity = productQuantity,
            price = price,
            productVisibility = productVisibility,
            innerPadding = innerPadding,
            onSaveData = { name, productDetail, price, quantity, imageUri, productVisibility ->
                dialogMessage = ""
                if (name.length < 5) {
                    dialogMessageId = R.string.invalidProductName
                    showDialog = true
                } else if (productDetail.length < 5) {
                    dialogMessageId = R.string.invalidProductDesc
                    showDialog = true
                } else if (price <= 0) {
                    dialogMessageId = R.string.invalidProductPrice
                    showDialog = true
                } else if (quantity <= 0) {
                    dialogMessageId = R.string.invalidProductQuantity
                    showDialog = true
                } else {
                    loading = true
                    if (imageUri.isEmpty()) {
                        viewModel.saveUpdateProduct(productID, name,
                            productDetail,
                            imageName,
                            price,
                            quantity,
                            productVisibility,
                            onSuccess = { productData ->
                                loading = false
                                savedShowDialog = true
                                savedDialogMessage =
                                    "$name ${context.getString(R.string.product_saved_successfully)}"
                            },
                            onFailure = { e ->
                                printError(e)
                                loading = false
                                showDialog = true
                                dialogMessage = e.message ?: ""
                            })
                    } else {
                        viewModel.uploadImageToStorage(
                            imageUri = imageUri.toUri(),
                            onSuccess = { imageName ->
                                viewModel.saveUpdateProduct(productID, name,
                                    productDetail,
                                    imageName,
                                    price,
                                    quantity,
                                    productVisibility,
                                    onSuccess = { productData ->
                                        loading = false
                                        savedShowDialog = true
                                        savedDialogMessage =
                                            "$name ${context.getString(R.string.product_saved_successfully)}"
                                    },
                                    onFailure = { e ->
                                        printError(e)
                                        loading = false
                                        showDialog = true
                                        dialogMessage = e.message ?: ""
                                    })
                            },
                            onFailure = { e ->
                                loading = false
                                showDialog = true
                                dialogMessage = e.message ?: ""
                            }
                        )
                    }
                }
            }, navigateBack = navigateBack
        )
    }
    if (loading) {
        LoadingIndicator()
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
    if (savedShowDialog) {
        ShowAlertDialog(
            dialogMessage = savedDialogMessage,
            onDismiss = {
                savedShowDialog = false
                savedDialogMessage = ""
                navigateBack()
            }
        )
    }
}
