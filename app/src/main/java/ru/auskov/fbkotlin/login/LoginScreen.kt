package ru.auskov.fbkotlin.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.components.RoundedButton
import ru.auskov.fbkotlin.components.RoundedTextInput
import ru.auskov.fbkotlin.ui.theme.Black10
import ru.auskov.fbkotlin.ui.theme.Purple40

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

    Image(
        painter = painterResource(R.drawable.login_bg),
        contentDescription = "Login",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black10)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Dogs by Nature",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Purple40,
            modifier = Modifier.padding(bottom = 50.dp),
        )

        RoundedTextInput(
            label = "Email",
            value = email.value
        ) {
            email.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedTextInput(
            label = "Password",
            value = password.value
        ) {
            password.value = it
        }

        Spacer(modifier = Modifier.height(50.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RoundedButton(name = "Sign In") {

            }

            Spacer(modifier = Modifier.height(10.dp))

            RoundedButton(name = "Sign Up") {

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