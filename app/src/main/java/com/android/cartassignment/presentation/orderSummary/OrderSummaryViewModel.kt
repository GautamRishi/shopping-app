package com.android.cartassignment.presentation.orderSummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cartassignment.core.Constants.ORDER_ID_FIELD
import com.android.cartassignment.core.Constants.ORDER_TABLE
import com.android.cartassignment.domain.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.isNotEmpty

@HiltViewModel
class OrderSummaryViewModel @Inject constructor(
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    fun getOrderData(
        cartID: String,
        onSuccess: (Order) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val documents =
                    firestore.collection(ORDER_TABLE).whereEqualTo(ORDER_ID_FIELD, cartID).get()
                        .await()

                val cart = documents.toObjects(Order::class.java)
                if (cart.isNotEmpty()) {
                    onSuccess(cart[0])
                } else {
                    onFailure(Exception("Order not found"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}