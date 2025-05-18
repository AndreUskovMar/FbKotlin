package ru.auskov.fbkotlin.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.utils.firebase.AuthManager
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {
    val user = mutableStateOf<FirebaseUser?>(null)
    val email = mutableStateOf("andreuskov2211@gmail.com")
    val password = mutableStateOf("12345678qQ!")
    val error = mutableStateOf("")

    val resetPasswordState = mutableStateOf(false)
    val isShownAlertDialog = mutableStateOf(false)

    fun signIn(
        onSignInSuccess: (MainScreenDataObject) -> Unit
    ) {
        authManager.signIn(
            email.value,
            password.value,
            onSignInSuccess = { navData ->
                error.value = ""
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
                error.value = ""
                onSignUpSuccess(navData)
            },
            onSignUpError = { e ->
                error.value = e
            }
        )
    }

    fun resetPassword() {
        authManager.resetPassword(
            email.value,
            onResetPasswordSuccess = {
                resetPasswordState.value = false
                isShownAlertDialog.value = true
            },
            onResetPasswordError = { e ->
                error.value = e
            }
        )
    }

    fun checkUserState() {
        user.value = authManager.getCurrentUser()
    }

    fun logout() {
        authManager.logout()
        user.value = null
    }
}