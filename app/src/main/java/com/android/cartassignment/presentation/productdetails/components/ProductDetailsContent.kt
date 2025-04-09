package com.android.cartassignment.presentation.productdetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.cartassignment.R
import com.android.cartassignment.components.ActionButton
import com.android.cartassignment.components.LoadingIndicator
import com.android.cartassignment.core.Constants.PREFS_CART_ID
import com.android.cartassignment.core.SharedPreferencesHelper
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.domain.model.Products
import com.android.cartassignment.domain.repository.AuthRepository
import com.android.cartassignment.navigation.Route.ProductCart
import com.android.cartassignment.presentation.productdetails.ProductDetailsViewModel

@Composable
fun ProductDetailsContent(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    repo: AuthRepository = hiltViewModel<ProductDetailsViewModel>().repo,
    innerPadding: PaddingValues,
    product: Products?,
    imageUrl: String?,
    navigate: (String) -> Unit
) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        product?.let {
            Card(
                modifier = Modifier
                    .height(350.dp)
                    .padding(4.dp),
                backgroundColor = Color.White,
                shape = RoundedCornerShape(10.dp),
                elevation = 3.dp,
                border = BorderStroke(1.dp, colorResource(R.color.gray))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.loading_img),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = product.productDetails,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = product.productName,
                    fontSize = 24.sp,
                    color = colorResource(id = R.color.product_name),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = product.productDetails,
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.product_description),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Price: ₹${product.price}",
                    fontSize = 22.sp,
                    color = colorResource(id = R.color.product_price)
                )
                Spacer(
                    modifier = Modifier.height(40.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ActionButton(
                        onActionButtonClick = {
                            loading = true
                            viewModel.addUpdateCart(
                                product = product,
                                imageUrl = imageUrl.toString(),
                                onSuccess = { cartID ->
                                    loading = false
                                    SharedPreferencesHelper.saveString(
                                        context,
                                        PREFS_CART_ID,
                                        cartID
                                    )
                                    navigate(ProductCart.route)
                                },
                                onFailure = { exception ->
                                    loading = false
                                    showDialog = true
                                    dialogMessage = exception.message ?: ""
                                }
                            )
                        },
                        enabled = true, resourceId = R.string.add_to_cart
                    )
                }
            }
        }
    }
    if (loading) {
        LoadingIndicator()
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