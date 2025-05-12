package ru.auskov.fbkotlin.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.utils.firebase.AuthManager
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {
    val email = mutableStateOf("andreuskov2211@gmail.com")
    val password = mutableStateOf("12345678qQ!")
    val error = mutableStateOf("")

    fun signIn(
        onSignInSuccess: (MainScreenDataObject) -> Unit
    ) {
        authManager.signIn(
            email.value,
            password.value,
            onSignInSuccess = { navData ->
                onSignInSuccess(navData)
            },
            onSignInError = { e ->
                error.value = e
            }
        )
    }

    fun signUp(
        onSignUpSuccess: (MainScreenDataObject) -> Unit
    ) {
        authManager.signUp(
            email.value,
            password.value,
            onSignUpSuccess = { navData ->
                onSignUpSuccess(navData)
            },
            onSignUpError = { e ->
                error.value = e
            }
        )
    }
}