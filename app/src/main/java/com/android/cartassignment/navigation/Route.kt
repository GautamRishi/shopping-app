package com.android.cartassignment.navigation

import com.android.cartassignment.domain.model.Products
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val route: String) {

    @Serializable
    object Splash : Route("splash")

    @Serializable
    object SignIn : Route("sign_in")

    @Serializable
    object SignUp : Route("sign_up")

    @Serializable
    object ProductList : Route("product_list")

    @Serializable
    object ProductDetails : Route("product_details/{productID}/{productName}") {
        fun createRoute(productID: String, productName: String) =
            "product_details/$productID/$productName"
    }

    @Serializable
    object ProductCart : Route("product_cart")

    @Serializable
    object AddUpdateProduct :
        Route("add_update_product/{productID}/{productName}/{productDetails}/{imageName}/{imageUrl}/{productQuantity}/{price}/{productVisibility}") {
        fun createRoute(
            productID: String,
            productName: String,
            productDetails: String,
            imageName: String,
            imageUrl: String,
            productQuantity: Int,
            price: Int,
            productVisibility: Boolean
        ) =
            "add_update_product/$productID/$productName/$productDetails/$imageName/$imageUrl/$productQuantity/$price/$productVisibility"
    }

    @Serializable
    object OrderSummary : Route("order_summary/{orderID}") {
        fun createRoute(orderID: String) =
            "order_summary/$orderID"
    }
}