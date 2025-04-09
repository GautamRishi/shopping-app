package com.android.cartassignment.domain.model

import com.android.cartassignment.core.Constants.DATE_TIME_FORMATTER
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class User(
    val userID: String = "",
    val userMailID: String = "",
    val userName: String = "",
    val userRole: String = "",
    val createdDate: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER))
)
