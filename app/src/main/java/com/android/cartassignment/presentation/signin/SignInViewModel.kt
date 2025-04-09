package com.android.cartassignment.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cartassignment.core.Constants.USER_MAIL_ID_FIELD
import com.android.cartassignment.core.Constants.USER_TABLE
import com.android.cartassignment.domain.model.Response
import com.android.cartassignment.domain.model.Response.Loading
import com.android.cartassignment.domain.model.User
import com.android.cartassignment.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

typealias SignInResponse = Response<Unit>

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
    private val _signInResponse = MutableStateFlow<SignInResponse>(Loading)
    val signInResponse: StateFlow<SignInResponse> get() = _signInResponse
    private val firestore = FirebaseFirestore.getInstance()

    fun signInWithEmailAndPassword(email: String, password: String) =
        viewModelScope.launch {
            _signInResponse.emit(Loading)
            try {
                val result = repo.signInWithEmailAndPassword(email, password)
                _signInResponse.emit(Response.Success(result))
            } catch (e: Exception) {
                _signInResponse.emit(Response.Failure(e))
            }
        }

    fun getUserData(
        email: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val documents = firestore.collection(USER_TABLE)
                    .whereEqualTo(USER_MAIL_ID_FIELD, email).get().await()
                var userData = documents.toObjects(User::class.java).toMutableList()
                if (userData.isNotEmpty()) {
                    onSuccess(userData[0])
                } else {
                    repo.signOut()
                    onFailure(Exception("User not found"))
                }
            } catch (e: Exception) {
                repo.signOut()
                onFailure(e)
            }
        }
    }
}