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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.components.RoundedButton
import ru.auskov.fbkotlin.components.RoundedTextInput
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.ui.theme.Black10
import ru.auskov.fbkotlin.ui.theme.Purple40
import ru.auskov.fbkotlin.ui.theme.Red

@Composable
fun LoginScreen(
    onNavigateToMainScreen: (MainScreenDataObject) -> Unit
) {
    // Initialize Firebase Auth
    val auth: FirebaseAuth = remember {
        Firebase.auth
    }

    val email = remember {
        mutableStateOf("andreuskov2211@gmail.com")
    }

    val password = remember {
        mutableStateOf("12345678qQ!")
    }

    val error = remember {
        mutableStateOf("")
    }

    //    Log.d("MyLog", "User signed in with email: ${auth.currentUser?.email}")

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
            style = TextStyle(
                fontSize = 46.sp,
                lineHeight = 50.sp,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                )
            ),
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

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = error.value,
            color = Red,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RoundedButton(name = "Sign In") {
                signIn(
                    auth,
                    email.value,
                    password.value,
                    onSignInSuccess = { navData ->
                        onNavigateToMainScreen(navData)
                    },
                    onSignInError = { e ->
                        error.value = e
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            RoundedButton(name = "Sign Up") {
                signUp(
                    auth,
                    email.value,
                    password.value,
                    onSignUpSuccess = { navData ->
                        onNavigateToMainScreen(navData)
                    },
                    onSignUpError = { e ->
                        error.value = e
                    }
                )
            }
        }
    }
}

private fun signUp(
    auth: FirebaseAuth,
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

private fun signIn(
    auth: FirebaseAuth,
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