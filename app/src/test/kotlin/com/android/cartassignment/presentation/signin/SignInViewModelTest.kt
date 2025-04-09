package com.android.cartassignment.presentation.signin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.cartassignment.domain.model.Response
import com.android.cartassignment.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class SignInViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val authRepository = mock(AuthRepository::class.java)
    private val viewModel = SignInViewModel(authRepository)

    @Test
    fun `signInWithEmailAndPassword emits Loading and Success`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val response = Unit

        `when`(authRepository.signInWithEmailAndPassword(email, password)).thenReturn(
            response
        )

        viewModel.signInWithEmailAndPassword(email, password)

        val responsesLoading = viewModel.signInResponse.value
        val responsesSuccess = Response.Success(response)
        assertEquals(Response.Loading, responsesLoading)
        assertEquals(Response.Success(response), responsesSuccess)
    }

    @Test
    fun `signInWithEmailAndPassword emits Loading and Failure`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val exception = RuntimeException("Sign in failed")

        `when`(authRepository.signInWithEmailAndPassword(email, password)).thenThrow(exception)

        viewModel.signInWithEmailAndPassword(email, password)

        val responsesLoading = viewModel.signInResponse.value
        assertEquals(Response.Loading, responsesLoading)

        val responsesFail = Response.Failure(exception)
        assertEquals(Response.Failure(exception), responsesFail)
    }
}