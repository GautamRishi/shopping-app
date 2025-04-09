package com.android.cartassignment.presentation.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cartassignment.core.Constants.ORDER_STATUS_CREATED
import com.android.cartassignment.core.Constants.ORDER_STATUS_FIELD
import com.android.cartassignment.core.Constants.ORDER_TABLE
import com.android.cartassignment.core.Constants.PRODUCT_ID_FIELD
import com.android.cartassignment.core.Constants.PRODUCT_TABLE
import com.android.cartassignment.core.Constants.USER_MAIL_ID_FIELD
import com.android.cartassignment.core.generateID
import com.android.cartassignment.domain.model.CartItems
import com.android.cartassignment.domain.model.Order
import com.android.cartassignment.domain.model.Products
import com.android.cartassignment.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    val repo: AuthRepository
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    fun getProductDetails(
        productID: String,
        onSuccess: (Products) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val documents = firestore.collection(PRODUCT_TABLE)
                    .whereEqualTo(PRODUCT_ID_FIELD, productID)
                    .get().await()

                val products = documents.toObjects(Products::class.java)
                if (products.isNotEmpty()) {
                    onSuccess(products[0])
                } else {
                    onFailure(Exception("Product not found"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun addUpdateCart(
        product: Products,
        imageUrl: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val serverCartData = getCartData()
                if (serverCartData.isNotEmpty()) {
                    val updatedCart: Order
                    val cart = serverCartData[0]
                    val existingItem = cart.cartItems.find { it.productID == product.productID }
                    if (existingItem != null) {
                        val updatedItems = cart.cartItems.map {
                            if (it.productID == product.productID) {
                                it.copy(quantity = it.quantity + 1)
                            } else {
                                it
                            }
                        }
                        updatedCart = cart.copy(
                            cartItems = updatedItems,
                            totalCartAmount = cart.totalCartAmount + product.price
                        )
                    } else {
                        val newItem = CartItems(
                            productID = product.productID,
                            productName = product.productName,
                            productDescription = product.productDetails,
                            imageUrl = imageUrl,
                            price = product.price,
                            quantity = 1
                        )
                        val updatedItems = cart.cartItems + newItem
                        updatedCart = cart.copy(
                            cartItems = updatedItems,
                            totalCartAmount = cart.totalCartAmount + product.price
                        )
                    }
                    firestore.collection(ORDER_TABLE).document(cart.orderID).set(updatedCart).await()
                    onSuccess(updatedCart.orderID)
                } else {
                    val cartItem = CartItems(
                        productID = product.productID,
                        productName = product.productName,
                        productDescription = product.productDetails,
                        imageUrl = imageUrl,
                        price = product.price,
                        quantity = 1
                    )
                    val newCart = Order(
                        orderID = generateID("O"),
                        cartItems = listOf(cartItem),
                        totalCartAmount = product.price,
                        orderStatus = ORDER_STATUS_CREATED,
                        userMailID = repo.getUserMailID().toString()
                    )
                    firestore.collection(ORDER_TABLE).document(newCart.orderID).set(newCart).await()
                    onSuccess(newCart.orderID)
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    private suspend fun getCartData(): MutableList<Order> {
        val documents = firestore.collection(ORDER_TABLE)
            .whereEqualTo(USER_MAIL_ID_FIELD, repo.getUserMailID())
            .whereEqualTo(ORDER_STATUS_FIELD, ORDER_STATUS_CREATED)
            .get().await()
        return documents.toObjects(Order::class.java).toMutableList()
    }

    suspend fun getImageUrl(imagePath: String): String {
        val storageReference = firebaseStorage.reference.child(imagePath)
        return storageReference.downloadUrl.await().toString()
    }
}