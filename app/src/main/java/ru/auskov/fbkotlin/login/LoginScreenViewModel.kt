package ru.auskov.fbkotlin.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.utils.firebase.AuthManager
import ru.auskov.fbkotlin.utils.store.StoreManager
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val storeManager: StoreManager
) : ViewModel() {
    val user = mutableStateOf<FirebaseUser?>(null)
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val error = mutableStateOf("")

    val resetPasswordState = mutableStateOf(false)
    val isShownAlertDialog = mutableStateOf(false)

    fun getCurrentEmail() {
        email.value = storeManager.getString(StoreManager.EMAIL_KEY, "")
    }

    fun saveLastEmail() {
        storeManager.saveString(StoreManager.EMAIL_KEY, email.value)
    }

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