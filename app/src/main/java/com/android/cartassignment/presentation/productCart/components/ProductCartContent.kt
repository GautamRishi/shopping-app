package com.android.cartassignment.presentation.productCart.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.cartassignment.R
import com.android.cartassignment.components.AddressInputDialog
import com.android.cartassignment.components.LoadingIndicator
import com.android.cartassignment.core.Constants.EMPTY_STRING
import com.android.cartassignment.core.Constants.PREFS_CART_ID
import com.android.cartassignment.core.SharedPreferencesHelper
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.domain.model.Order
import com.android.cartassignment.navigation.Route.OrderSummary
import com.android.cartassignment.presentation.productCart.ProductCartViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductCartContent(
    viewModel: ProductCartViewModel = hiltViewModel(),
    navigate: (String) -> Unit,
    innerPadding: PaddingValues,
    cartData: Order
) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var showAddressDialog by remember { mutableStateOf(false) }
    var updatedCartItems by remember { mutableStateOf(cartData.cartItems) }
    var totalAmount by remember { mutableIntStateOf(cartData.totalCartAmount) }

    if (updatedCartItems.isEmpty()) {
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
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp)
            ) {
                items(
                    count = updatedCartItems.size,
                    itemContent = { index ->
                        val item = updatedCartItems[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 3.dp, bottom = 3.dp),
                            backgroundColor = Color.White,
                            elevation = 3.dp,
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(item.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    error = painterResource(R.drawable.loading_img),
                                    placeholder = painterResource(R.drawable.loading_img),
                                    contentDescription = item.productDescription,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(120.dp),
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 12.dp)
                                ) {
                                    Text(
                                        text = item.productName,
                                        color = colorResource(R.color.product_name),
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = item.productDescription,
                                        color = colorResource(R.color.product_description),
                                        fontSize = 12.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Price: ₹${item.price}",
                                        color = colorResource(R.color.product_description),
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(42.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            modifier = Modifier
                                                .padding(bottom = 10.dp),
                                            onClick = {
                                                updatedCartItems =
                                                    updatedCartItems.toMutableList().apply {
                                                        this[index] =
                                                            this[index].copy(quantity = this[index].quantity - 1)
                                                    }
                                                totalAmount = totalAmount - item.price
                                            }, enabled = item.quantity > 1
                                        ) {
                                            Icon(
                                                Icons.Filled.Minimize,
                                                contentDescription = "Decrease",
                                                tint = if (item.quantity > 1) Color.Black else Color.Gray
                                            )
                                        }
                                        Text(
                                            text = "${item.quantity}",
                                            color = colorResource(R.color.product_description),
                                            fontSize = 24.sp,
                                            maxLines = 1,
                                            modifier = Modifier
                                                .width(48.dp)
                                                .padding(5.dp)
                                                .background(
                                                    color = Color.LightGray
                                                ),
                                            textAlign = TextAlign.Center
                                        )
                                        IconButton(
                                            onClick = {
                                                updatedCartItems =
                                                    updatedCartItems.toMutableList().apply {
                                                        this[index] =
                                                            this[index].copy(quantity = this[index].quantity + 1)
                                                    }
                                                totalAmount = totalAmount + item.price
                                            }
                                        ) {
                                            Icon(
                                                Icons.Filled.Add,
                                                contentDescription = "Increase",
                                                tint = Color.Black
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        IconButton(
                                            onClick = {
                                                loading = true
                                                var deletedCartItem =
                                                    updatedCartItems.toMutableList().apply {
                                                        removeAt(index)
                                                    }
                                                var deletedTotalAmount =
                                                    deletedCartItem.sumOf { it.quantity * it.price }
                                                viewModel.deleteCartItem(
                                                    cartData = cartData,
                                                    updatedCartItems = deletedCartItem,
                                                    totalAmount = deletedTotalAmount,
                                                    onSuccess = {
                                                        loading = false
                                                        updatedCartItems = deletedCartItem
                                                        totalAmount = deletedTotalAmount
                                                    },
                                                    onFailure = { exception ->
                                                        loading = false
                                                        showDialog = true
                                                        dialogMessage =
                                                            exception.message ?: ""
                                                    }
                                                )
                                                updatedCartItems =
                                                    updatedCartItems.toMutableList().apply {
                                                        removeAt(index)
                                                    }
                                                totalAmount =
                                                    updatedCartItems.sumOf { it.quantity * it.price }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Filled.Delete,
                                                contentDescription = "Delete",
                                                tint = Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Amt - ₹${totalAmount}",
                    fontSize = 20.sp,
                    color = colorResource(R.color.product_price),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        viewModel.getCustomerAddress(
                            onSuccess = { addressData ->
                                if (addressData.isNotEmpty()) {
                                    loading = true
                                    val address =
                                        "${addressData[0].address} ${addressData[0].pinCode}"
                                    viewModel.placeOrder(
                                        cartData = cartData,
                                        updatedCartItems = updatedCartItems,
                                        totalAmount = totalAmount,
                                        address = address,
                                        onSuccess = {
                                            SharedPreferencesHelper.saveString(
                                                context,
                                                PREFS_CART_ID,
                                                EMPTY_STRING
                                            )
                                            loading = false
                                            showDialog = true
                                            dialogMessage = "Order Placed Successfully"
                                        },
                                        onFailure = { exception ->
                                            loading = false
                                            showDialog = true
                                            dialogMessage = exception.message ?: ""
                                        }
                                    )
                                } else {
                                    showAddressDialog = true
                                }
                            },
                            onFailure = { exception ->
                                loading = false
                                showDialog = true
                                dialogMessage = exception.message ?: ""
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.yellow_900)
                    ),
                    modifier = Modifier
                        .width(135.dp)
                        .height(42.dp)
                ) {
                    Text(
                        text = stringResource(R.string.place_order),
                        color = Color.White
                    )
                }
            }
        }
    }
    if (loading) {
        LoadingIndicator()
    }

    if (showAddressDialog) {
        AddressInputDialog(
            onDismiss = { showAddressDialog = false },
            onSave = { address, pinCode ->
                loading = true
                viewModel.saveAddress(
                    address = address,
                    pinCode = pinCode,
                    onSuccess = {
                        val finalAddress = "$address $pinCode"
                        viewModel.placeOrder(
                            cartData = cartData,
                            updatedCartItems = updatedCartItems,
                            totalAmount = totalAmount,
                            address = finalAddress,
                            onSuccess = {
                                SharedPreferencesHelper.saveString(
                                    context,
                                    PREFS_CART_ID,
                                    EMPTY_STRING
                                )
                                showAddressDialog = false
                                loading = false
                                showDialog = true
                                dialogMessage = "Order Placed Successfully"
                            },
                            onFailure = { exception ->
                                showAddressDialog = false
                                loading = false
                                showDialog = true
                                dialogMessage = exception.message ?: ""
                            }
                        )
                    },
                    onFailure = { exception ->
                        showAddressDialog = false
                        loading = false
                        showDialog = true
                        dialogMessage = exception.message ?: ""
                    }
                )
            }
        )
    }

    if (showDialog) {
        ShowAlertDialog(
            dialogMessage = dialogMessage,
            onDismiss = {
                showDialog = false
                dialogMessage = ""
                navigate(OrderSummary.createRoute(cartData.orderID))
            }
        )
    }
}
