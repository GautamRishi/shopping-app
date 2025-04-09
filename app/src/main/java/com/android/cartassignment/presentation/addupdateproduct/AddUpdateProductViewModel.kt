package com.android.cartassignment.presentation.addupdateproduct

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cartassignment.core.Constants.PRODUCT_IMAGE_TABLE
import com.android.cartassignment.core.Constants.PRODUCT_TABLE
import com.android.cartassignment.core.generateID
import com.android.cartassignment.domain.model.Products
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class AddUpdateProductViewModel @Inject constructor(
) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val fireStorage = FirebaseStorage.getInstance()

    fun saveUpdateProduct(
        productID: String,
        name: String,
        productDetail: String,
        imageName: String,
        price: Int,
        quantity: Int,
        productVisibility: Boolean,
        onSuccess: (Products) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val productData = Products(
                    productID = if (productID.isEmpty()) generateID("P") else productID,
                    productName = name,
                    productDetails = productDetail,
                    imageName = imageName,
                    price = price,
                    productQuantity = quantity,
                    productVisibility = productVisibility
                )
                firestore.collection(PRODUCT_TABLE).document(productData.productID).set(productData)
                    .await()
                onSuccess(productData)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun uploadImageToStorage(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val imageName = imageUri.lastPathSegment + generateID("_")
                val imagePath = "${PRODUCT_IMAGE_TABLE}$imageName"
                val storageRef = fireStorage.reference.child(imagePath)
                storageRef.putFile(imageUri).await()
                onSuccess(imageName)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun deleteProduct(
        productID: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                firestore.collection(PRODUCT_TABLE).document(productID).delete().await()
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}