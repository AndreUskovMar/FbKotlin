package ru.auskov.fbkotlin.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.fbkotlin.utils.firebase.AuthManager
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

}