package com.android.cartassignment.presentation.productlist.components

import android.net.Uri
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.cartassignment.R
import com.android.cartassignment.core.Constants.PREFS_PRODUCT_ID
import com.android.cartassignment.core.Constants.PREFS_PRODUCT_NAME
import com.android.cartassignment.core.Constants.PRODUCT_IMAGE_TABLE
import com.android.cartassignment.core.Constants.USER_TYPE_ADMIN
import com.android.cartassignment.core.SharedPreferencesHelper
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.domain.model.Products
import com.android.cartassignment.navigation.Route.AddUpdateProduct
import com.android.cartassignment.navigation.Route.ProductDetails
import com.android.cartassignment.presentation.productlist.ProductListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductListContent(
    viewModel: ProductListViewModel = hiltViewModel(),
    navigate: (String) -> Unit,
    userType: String,
    products: List<Products>,
    innerPadding: PaddingValues
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageUrlCache = remember { mutableMapOf<String, String>() }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (userType.equals(USER_TYPE_ADMIN, ignoreCase = true)) {
            Button(
                onClick = {
                    navigate(AddUpdateProduct.createRoute("", "", "", "", "", 0, 0, false))
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.yellow_800)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_product),
                    tint = colorResource(id = R.color.white)
                )
            }
        }
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    top = 8.dp
                ), columns = GridCells.Adaptive(150.dp)
        ) {

            items(count = products.count(), itemContent = { index ->
                val product = products[index]
                var imageUrl by remember { mutableStateOf<String>("") }
                if (product.imageName.isNotEmpty()) {
                    imageUrl = imageUrlCache[product.imageName] ?: ""
                    if (imageUrl.isEmpty()) {
                        LaunchedEffect(product.imageName) {
                            coroutineScope.launch {
                                imageUrl =
                                    viewModel.getImageUrl("${PRODUCT_IMAGE_TABLE}${product.imageName}")
                                imageUrlCache[product.imageName] = imageUrl
                            }
                        }
                    }
                }
                Card(
                    onClick = {
                        if (userType.equals(USER_TYPE_ADMIN, ignoreCase = true)) {
                            navigate(
                                AddUpdateProduct.createRoute(
                                    product.productID,
                                    product.productName,
                                    product.productDetails,
                                    product.imageName,
                                    Uri.encode(imageUrl),
                                    product.productQuantity,
                                    product.price,
                                    product.productVisibility
                                )
                            )
                        } else {
                            SharedPreferencesHelper.saveString(
                                context,
                                PREFS_PRODUCT_ID,
                                product.productID
                            )
                            SharedPreferencesHelper.saveString(
                                context,
                                PREFS_PRODUCT_NAME,
                                product.productName
                            )
                            navigate(
                                ProductDetails.createRoute(
                                    productID = product.productID,
                                    productName = product.productName
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .height(260.dp)
                        .padding(4.dp),
                    backgroundColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    elevation = 3.dp,
                    border = BorderStroke(1.dp, colorResource(R.color.gray))
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl.ifEmpty { R.drawable.loading_img })
                                    .crossfade(true)
                                    .build(),
                                error = painterResource(R.drawable.image_place_holder),
                                placeholder = painterResource(R.drawable.loading_img),
                                contentDescription = product.productDetails,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                contentScale = ContentScale.Crop
                            )

                            Column(
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    end = 12.dp,
                                    top = 12.dp
                                ),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = product.productName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.product_name),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = product.productDetails,
                                    fontSize = 13.sp,
                                    color = colorResource(id = R.color.product_description),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Price: ₹${product.price}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.product_price)
                                    )
                                    if (userType.equals(USER_TYPE_ADMIN, ignoreCase = true)) {
                                        Text(
                                            text = "Qty: ${product.productQuantity}",
                                            fontSize = 14.sp,
                                            color = colorResource(id = R.color.product_price)
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            })
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