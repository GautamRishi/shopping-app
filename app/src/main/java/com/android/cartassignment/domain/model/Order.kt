package com.android.cartassignment.domain.model

import com.android.cartassignment.core.Constants.DATE_TIME_FORMATTER
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Order(
    val orderID: String = "",
    val cartItems: List<CartItems> = emptyList(),
    val totalCartAmount: Int = 0,
    val address: String = "",
    val orderStatus: String = "",
    val userMailID: String = "",
    val createdDate: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER))
)
