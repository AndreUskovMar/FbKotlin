package ru.auskov.fbkotlin.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen() {
    // Initialize Firebase Auth
    val auth: FirebaseAuth = Firebase.auth

    val email = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    Log.d("MyLog", "User signed in with email: ${auth.currentUser?.email}")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = email.value, onValueChange = {
                email.value = it
            })

            Spacer(modifier = Modifier.height(10.dp))

            TextField(value = password.value, onValueChange = {
                password.value = it
            })
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                signIn(auth, email.value, password.value)
            }) {
                Text("Sign In")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                signUp(auth, email.value, password.value)
            }) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                logout(auth)
            }) {
                Text("Logout")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                deleteAccount(auth, email.value, password.value)
            }) {
                Text("Delete")
            }
        }
    }
}

private fun signUp(auth: FirebaseAuth, email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("MyLog", "createUserWithEmail:success")
                // val user = auth.currentUser
            } else {
                // If sign in fails, display a message to the user.
                Log.w("MyLog", "createUserWithEmail:failure", task.exception)
            }
        }
}

private fun signIn(auth: FirebaseAuth, email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("MyLog", "signInWithEmail:success")
                // val user = auth.currentUser
            } else {
                // If sign in fails, display a message to the user.
                Log.w("MyLog", "signInWithEmail:failure", task.exception)
            }
        }
}

private fun logout(auth: FirebaseAuth) {
    auth.signOut()
}

private fun deleteAccount(auth: FirebaseAuth, email: String, password: String) {
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