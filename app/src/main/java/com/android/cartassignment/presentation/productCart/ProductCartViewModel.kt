package com.android.cartassignment.presentation.productCart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cartassignment.core.generateID
import com.android.cartassignment.domain.model.Address
import com.android.cartassignment.domain.model.CartItems
import com.android.cartassignment.domain.model.Order
import com.android.cartassignment.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.isNotEmpty

@HiltViewModel
class ProductCartViewModel @Inject constructor(
    val repo: AuthRepository
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val orderTable = "order"
    private val addressTable = "address"
    private val orderIDField = "orderID"
    private val userMailIDField = "userMailID"
    private val orderStatusPlaced = "Placed"


    fun getCartData(
        cartID: String,
        onSuccess: (Order) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val documents =
                    firestore.collection(orderTable).whereEqualTo(orderIDField, cartID).get().await()

                val cart = documents.toObjects(Order::class.java)
                if (cart.isNotEmpty()) {
                    onSuccess(cart[0])
                } else {
                    onFailure(Exception("Cart not found"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun getCustomerAddress(
        onSuccess: (List<Address>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val documents = firestore.collection(addressTable)
                    .whereEqualTo(userMailIDField, repo.getUserMailID()).get().await()
                var addressData = documents.toObjects(Address::class.java).toMutableList()
                onSuccess(addressData)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun saveAddress(
        address: String,
        pinCode: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val newAddress = Address(
                    addressID = generateID("A"),
                    address = address,
                    pinCode = pinCode,
                    userMailID = repo.getUserMailID().toString()
                )
                firestore.collection(addressTable).document(newAddress.addressID).set(newAddress)
                    .await()
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun placeOrder(
        cartData: Order,
        updatedCartItems: List<CartItems>,
        totalAmount: Int,
        address: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updatedCart = cartData.copy(
                    cartItems = updatedCartItems,
                    totalCartAmount = totalAmount,
                    orderStatus = orderStatusPlaced,
                    address = address
                )
                firestore.collection(orderTable).document(cartData.orderID).set(updatedCart).await()
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun deleteCartItem(
        cartData: Order,
        updatedCartItems: List<CartItems>,
        totalAmount: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updatedCart = cartData.copy(
                    cartItems = updatedCartItems,
                    totalCartAmount = totalAmount
                )
                firestore.collection(orderTable).document(cartData.orderID).set(updatedCart).await()
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}