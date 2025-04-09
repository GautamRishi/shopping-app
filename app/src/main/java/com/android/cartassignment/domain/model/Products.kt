package com.android.cartassignment.domain.model

import com.android.cartassignment.core.Constants.DATE_TIME_FORMATTER
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Products(
    val productID: String = "",
    val productName: String = "",
    val productDetails: String = "",
    val imageName: String = "",
    val productQuantity: Int = 0,
    val price: Int = 0,
    val productVisibility: Boolean = false,
    val createdDate: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER))
)
