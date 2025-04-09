package com.android.cartassignment.presentation.splash

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.cartassignment.navigation.Route.ProductList
import com.android.cartassignment.navigation.Route.SignIn

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(), navigateAndClear: (String) -> Unit
) {
    if (viewModel.isUserSignedOut) {
        navigateAndClear(SignIn.route)
    } else {
        navigateAndClear(ProductList.route)
    }
}