package ru.auskov.fbkotlin.utils.firebase

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import javax.inject.Singleton

@Singleton
class AuthManager(
    private val auth: FirebaseAuth,
) {
    fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: (MainScreenDataObject) -> Unit,
        onSignUpError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            return onSignUpError("Email and password cannot be empty")
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSignUpSuccess(
                    MainScreenDataObject(
                        task.result.user?.uid ?: "",
                        task.result.user?.email ?: email,
                    )
                )
            }
            .addOnFailureListener {
                onSignUpError(it.localizedMessage ?: "Sign Up Error")
            }
    }

    fun signIn(
        email: String,
        password: String,
        onSignInSuccess: (MainScreenDataObject) -> Unit,
        onSignInError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            return onSignInError("Email and password cannot be empty")
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSignInSuccess(
                    MainScreenDataObject(
                        task.result.user?.uid ?: "",
                        task.result.user?.email ?: email,
                    )
                )
            }
            .addOnFailureListener {
                onSignInError(it.localizedMessage ?: "Sign In Error")
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun logout() {
        auth.signOut()
    }

    fun deleteAccount(email: String, password: String) {
        if (auth.currentUser !== null) {
            val credentials = EmailAuthProvider.getCredential(email, password)

            auth.currentUser?.reauthenticate(credentials)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.delete()!!
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("MyLog", "delete:success")
                                    // val user = auth.currentUser
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("MyLog", "delete:failure", it.exception)
                                }
                            }
                    } else {
                        Log.w("MyLog", "reauthenticate:failure", task.exception)
                    }
                }
        }
    }
}