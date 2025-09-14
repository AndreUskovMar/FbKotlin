package ru.auskov.fbkotlin.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.fbkotlin.utils.firebase.AuthManager
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val authManager: AuthManager
): ViewModel() {
    fun resetPassword(
        email: String,
        onResetPasswordSuccess: () -> Unit,
        onResetPasswordError: (String) -> Unit,
    ) {
        authManager.resetPassword(
            email,
            onResetPasswordSuccess,
            onResetPasswordError
        )
    }

    fun signOut() = authManager.logout()
}