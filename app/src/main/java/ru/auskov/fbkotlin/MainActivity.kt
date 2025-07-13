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
import dagger.hilt.android.AndroidEntryPoint
import ru.auskov.fbkotlin.add_book_screen.AddBookScreen
import ru.auskov.fbkotlin.add_book_screen.data.AddBookScreenObject
import ru.auskov.fbkotlin.details.DetailsScreen
import ru.auskov.fbkotlin.details.data.DetailsScreenObject
import ru.auskov.fbkotlin.login.LoginScreen
import ru.auskov.fbkotlin.login.data.LoginScreenObject
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.main.MainScreen
import ru.auskov.fbkotlin.ui.theme.FbKotlinTheme

@AndroidEntryPoint
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
                            navData = navData,
                            onBookClick = { book ->
                                navController.navigate(
                                    DetailsScreenObject(
                                        title = book.name,
                                        description = book.description,
                                        price = book.price.toString(),
                                        imageUrl = book.imageUrl
                                    )
                                )
                            },
                            onBookEditClick = { book ->
                                navController.navigate(
                                    AddBookScreenObject(
                                        key = book.key,
                                        name = book.name,
                                        description = book.description,
                                        categoryIndex = book.categoryIndex,
                                        price = book.price.toString(),
                                        imageUrl = book.imageUrl,
                                    )
                                )
                            }
                        ) {
                            navController.navigate(AddBookScreenObject())
                        }
                    }
                    composable<AddBookScreenObject> {navEntry ->
                        val navData = navEntry.toRoute<AddBookScreenObject>()
                        AddBookScreen(
                            navData,
                            onSavedSuccess = {
                                Log.d("MyLog", "Success")
                                navController.popBackStack()
                            }
                        )
                    }
                    composable<DetailsScreenObject> { navEntry ->
                        val navData = navEntry.toRoute<DetailsScreenObject>()
                        DetailsScreen(navData)
                    }
                }
            }
        }
    }
}