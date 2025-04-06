package ru.auskov.fbkotlin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.auskov.fbkotlin.add_book_screen.AddBookScreen
import ru.auskov.fbkotlin.add_book_screen.data.AddBookScreenObject
import ru.auskov.fbkotlin.login.LoginScreen
import ru.auskov.fbkotlin.login.data.LoginScreenObject
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.main.MainScreen
import ru.auskov.fbkotlin.ui.theme.FbKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            FbKotlinTheme {
                NavHost(navController = navController, startDestination = LoginScreenObject) {
                    composable<LoginScreenObject> {
                        LoginScreen { navData ->
                            navController.navigate(navData)
                        }
                    }
                    composable<MainScreenDataObject> { navEntry ->
                        val navData = navEntry.toRoute<MainScreenDataObject>()
                        MainScreen(
                            navData,
                            onBookEditClick = { book ->
                                navController.navigate(AddBookScreenObject(
                                    key = book.key,
                                    name = book.name,
                                    description = book.description,
                                    category = book.category,
                                    price = book.price,
                                    imageUrl = book.imageUrl,
                                ))
                            }
                        ) {
                            navController.navigate(AddBookScreenObject())
                        }
                    }
                    composable<AddBookScreenObject> {navEntry ->
                        val navData = navEntry.toRoute<AddBookScreenObject>()
                        AddBookScreen(navData) {
                            Log.d("MyLog", "Success")
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}