package com.android.cartassignment.presentation.orderSummary.components

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
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.cartassignment.R
import com.android.cartassignment.domain.model.Order

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderSummaryContent(
    innerPadding: PaddingValues,
    orderData: Order
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.gray))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Order ID: ${orderData.orderID}",
                        fontSize = 16.sp,
                        color = colorResource(R.color.product_price)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Order Status: ${orderData.orderStatus}",
                        fontSize = 16.sp,
                        color = colorResource(R.color.product_price)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total items : ${orderData.cartItems.sumOf { it.quantity }}",
                        fontSize = 16.sp,
                        color = colorResource(R.color.product_price)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Order amount : ₹${orderData.totalCartAmount}",
                        fontSize = 16.sp,
                        color = colorResource(R.color.product_price)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Address: ${orderData.address}",
                        fontSize = 16.sp,
                        color = colorResource(R.color.product_price)
                    )
                }
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
        items(
            count = orderData.cartItems.size,
            itemContent = { index ->
                val item = orderData.cartItems[index]
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 8.dp)
                        ) {
                            Text(
                                text = item.productName,
                                color = colorResource(R.color.product_name),
                                fontSize = 16.sp,
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
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Quantity: ${item.quantity}",
                                color = colorResource(R.color.product_description),
                                fontSize = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Total amount: ₹${item.quantity * item.price}",
                                color = colorResource(R.color.product_description),
                                fontSize = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.imageUrl)
                                .crossfade(true)
                                .build(),
                            error = painterResource(R.drawable.loading_img),
                            placeholder = painterResource(R.drawable.image_place_holder),
                            contentDescription = item.productDescription,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(120.dp),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    }
                }
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        )
    }
}
