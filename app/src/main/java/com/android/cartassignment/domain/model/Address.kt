package com.android.cartassignment.domain.model

import com.android.cartassignment.core.Constants.DATE_TIME_FORMATTER
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Address(
    val addressID: String = "",
    val address: String = "",
    val pinCode: String = "",
    val userMailID: String = "",
    val createdDate: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER))
)
