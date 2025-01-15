package ru.auskov.fbkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.auskov.fbkotlin.login.LoginScreen
// import ru.auskov.fbkotlin.main.MainScreen
import ru.auskov.fbkotlin.ui.theme.FbKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FbKotlinTheme {
                LoginScreen()
            }
        }
    }
}