package com.android.cartassignment.presentation.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cartassignment.core.Constants.CREATED_DATE_FIELD
import com.android.cartassignment.core.Constants.PRODUCT_TABLE
import com.android.cartassignment.core.Constants.PRODUCT_VISIBILITY_FIELD
import com.android.cartassignment.domain.model.Products
import com.android.cartassignment.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.ASCENDING
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    fun getAuthState(navigateToSignInScreen: () -> Unit) = viewModelScope.launch {
        repo.getAuthState().collect { isUserSignedOut ->
            if (isUserSignedOut) {
                navigateToSignInScreen()
            }
        }
    }

    fun signOut() = repo.signOut()

    fun getAllProducts(
        onSuccess: (List<Products>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val documents = firestore.collection(PRODUCT_TABLE)
                    .whereEqualTo(PRODUCT_VISIBILITY_FIELD, true)
                    .orderBy(CREATED_DATE_FIELD, ASCENDING)
                    .get().await()

                val productList = documents.toObjects(Products::class.java)
                onSuccess(productList)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    suspend fun getImageUrl(imagePath: String): String {
        val storageReference = firebaseStorage.reference.child(imagePath)
        return storageReference.downloadUrl.await().toString()
    }
}