package ru.auskov.fbkotlin.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.components.RoundedButton
import ru.auskov.fbkotlin.components.RoundedTextInput
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.ui.theme.Black10
import ru.auskov.fbkotlin.ui.theme.Purple40
import ru.auskov.fbkotlin.ui.theme.Red

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = hiltViewModel(),
    onNavigateToMainScreen: (MainScreenDataObject) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.checkUserState()
    }

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

        if (viewModel.user.value == null) {
            RoundedTextInput(
                label = "Email",
                value = viewModel.email.value
            ) {
                viewModel.email.value = it
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!viewModel.resetPasswordState.value) {
                RoundedTextInput(
                    label = "Password",
                    value = viewModel.password.value,
                    isPassword = true
                ) {
                    viewModel.password.value = it
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            Text(
                text = viewModel.error.value,
                color = Red,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (!viewModel.resetPasswordState.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RoundedButton(name = "Sign In") {
                        viewModel.signIn(
                            onSignInSuccess = { navData ->
                                onNavigateToMainScreen(navData)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    RoundedButton(name = "Sign Up") {
                        viewModel.signUp(
                            onSignUpSuccess = { navData ->
                                onNavigateToMainScreen(navData)
                            }
                        )
                    }
                }
            } else {
                RoundedButton(name = "Reset Password") {
                    viewModel.resetPassword()
                }
            }

            if (!viewModel.resetPasswordState.value) {
                Text(
                    text = "Forget Password",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        viewModel.error.value = ""
                        viewModel.resetPasswordState.value = true
                    }
                )
            }
        } else {
            RoundedButton(name = "Enter") {
                onNavigateToMainScreen(
                    MainScreenDataObject(
                        viewModel.user.value!!.uid,
                        viewModel.user.value!!.email!!,
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            RoundedButton(name = "Logout") {
                viewModel.logout()
            }
        }
    }
}