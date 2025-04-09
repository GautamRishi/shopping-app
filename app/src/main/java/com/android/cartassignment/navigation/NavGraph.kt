package com.android.cartassignment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.android.cartassignment.navigation.Route.AddUpdateProduct
import com.android.cartassignment.navigation.Route.OrderSummary
import com.android.cartassignment.navigation.Route.ProductCart
import com.android.cartassignment.navigation.Route.ProductDetails
import com.android.cartassignment.navigation.Route.ProductList
import com.android.cartassignment.navigation.Route.SignIn
import com.android.cartassignment.navigation.Route.SignUp
import com.android.cartassignment.navigation.Route.Splash
import com.android.cartassignment.presentation.addupdateproduct.AddUpdateProductScreen
import com.android.cartassignment.presentation.orderSummary.OrderSummaryScreen
import com.android.cartassignment.presentation.productCart.ProductCartScreen
import com.android.cartassignment.presentation.productdetails.ProductDetailsScreen
import com.android.cartassignment.presentation.productlist.ProductListScreen
import com.android.cartassignment.presentation.signin.SignInScreen
import com.android.cartassignment.presentation.signup.SignUpScreen
import com.android.cartassignment.presentation.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Splash.route
    ) {
        composable(Splash.route) {
            SplashScreen(
                navigateAndClear = navController::navigateAndClear
            )
        }
        composable(SignIn.route) {
            SignInScreen(
                navigate = navController::navigate,
                navigateAndClear = navController::navigateAndClear
            )
        }
        composable(SignUp.route) {
            SignUpScreen(
                navigateBack = navController::navigateUp,
                navigateAndClear = navController::navigateAndClear
            )
        }
        composable(ProductList.route) {
            ProductListScreen(
                navigateAndClear = navController::navigateAndClear,
                navigate = navController::navigate
            )
        }
        composable(ProductDetails.route){
            ProductDetailsScreen(
                navigate = navController::navigate,
                navigateBack = navController::navigateUp
            )
        }

        composable(ProductCart.route) {
            ProductCartScreen(
                navigate = navController::navigate,
                navigateBack = navController::navigateUp
            )
        }

        composable(route = AddUpdateProduct.route,
            arguments = listOf(
                navArgument("productID") { type = NavType.StringType },
                navArgument("productName") { type = NavType.StringType },
                navArgument("productDetails") { type = NavType.StringType },
                navArgument("imageName") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType },
                navArgument("productQuantity") { type = NavType.IntType },
                navArgument("price") { type = NavType.IntType },
                navArgument("productVisibility") { type = NavType.BoolType }
            )) { backStackEntry ->
            val productID = backStackEntry.arguments?.getString("productID") ?: ""
            val productName = backStackEntry.arguments?.getString("productName") ?: ""
            val productDetails = backStackEntry.arguments?.getString("productDetails") ?: ""
            val imageName = backStackEntry.arguments?.getString("imageName") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            val productQuantity = backStackEntry.arguments?.getInt("productQuantity") ?: 0
            val price = backStackEntry.arguments?.getInt("price") ?: 0
            val productVisibility =
                backStackEntry.arguments?.getBoolean("productVisibility") == true
            AddUpdateProductScreen(
                productID = productID,
                productName = productName,
                productDetails = productDetails,
                imageName = imageName,
                imageUrl = imageUrl,
                productQuantity = productQuantity,
                price = price,
                productVisibility = productVisibility,
                navigateBack = navController::navigateUp,
            )
        }
        composable(route = OrderSummary.route,
            arguments = listOf(
                navArgument("orderID") { type = NavType.StringType }
            )) { backStackEntry ->
            val orderID = backStackEntry.arguments?.getString("orderID") ?: ""
            OrderSummaryScreen(
                orderID = orderID,
                navigateAndClear = navController::navigateAndClear
            )
        }
    }
}

fun NavHostController.navigateAndClear(route: String) = navigate(route) {
    popUpTo(graph.startDestinationId) {
        inclusive = true
    }
    graph.setStartDestination(route)
}