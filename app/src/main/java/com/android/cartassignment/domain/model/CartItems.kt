package com.android.cartassignment.domain.model

data class CartItems(
    val productID: String = "",
    val productName: String = "",
    val productDescription: String = "",
    val imageUrl: String = "",
    val quantity: Int = 0,
    val price: Int = 0
)
