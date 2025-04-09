package com.android.cartassignment.presentation.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cartassignment.core.Constants.USER_TABLE
import com.android.cartassignment.core.Constants.USER_TYPE_CUSTOMER
import com.android.cartassignment.core.generateID
import com.android.cartassignment.core.launchCatching
import com.android.cartassignment.domain.model.Response
import com.android.cartassignment.domain.model.Response.Loading
import com.android.cartassignment.domain.model.User
import com.android.cartassignment.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

typealias SignUpResponseModel = Response<Unit>

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
    var signUpResponse by mutableStateOf<SignUpResponseModel>(Loading)
        private set
    private val firestore = FirebaseFirestore.getInstance()

    fun signUpWithEmailAndPassword(email: String, password: String) =
        viewModelScope.launch {
            signUpResponse = launchCatching {
                repo.signUpWithEmailAndPassword(email, password)
            }
        }

    fun saveUserData(
        name: String,
        email: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val userData = User(
                    userID = generateID("U"),
                    userMailID = email,
                    userName = name,
                    userRole = USER_TYPE_CUSTOMER
                )
                firestore.collection(USER_TABLE).document(userData.userID).set(userData).await()
                onSuccess(userData)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}