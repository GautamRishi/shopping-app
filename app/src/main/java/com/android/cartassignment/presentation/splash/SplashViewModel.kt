package com.android.cartassignment.presentation.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.android.cartassignment.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    val isUserSignedOut get() = repo.currentUser == null
}