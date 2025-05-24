package ru.auskov.fbkotlin.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.launch
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.main.components.BookListItem
import ru.auskov.fbkotlin.main.components.BottomMenu
import ru.auskov.fbkotlin.main.components.BottomMenuItem
import ru.auskov.fbkotlin.main.components.DrawerHeader
import ru.auskov.fbkotlin.main.components.DrawerList

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel  = hiltViewModel(),
    navData: MainScreenDataObject,
    onBookClick: (Book) -> Unit,
    onBookEditClick: (Book) -> Unit,
    onAdminClick: () -> Unit,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val isAdminState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        isAdmin { isAdmin ->
            isAdminState.value = isAdmin
        }
    }

    LaunchedEffect(Unit) {
        if (viewModel.booksList.value.isEmpty()) {
            viewModel.getAllBooks("Fantasy")
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(navData.email)
                DrawerList(
                    isAdminState.value,
                    onAdminClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }

                        onAdminClick()
                    },
                    onFavoritesClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }

                        viewModel.selectedItemState.value = BottomMenuItem.Favourites.title

                        viewModel.getFavoritesBooks()
                    },
                    onCategoryClick = {category ->
                        coroutineScope.launch {
                            drawerState.close()
                        }

                        viewModel.selectedItemState.value = BottomMenuItem.Home.title

                        viewModel.getAllBooks(category)
                    }
                )
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    selectedItem = viewModel.selectedItemState.value,
                    onHomeClick = {
                        viewModel.selectedItemState.value = BottomMenuItem.Home.title
                        viewModel.getAllBooks("Fantasy")
                    },
                    onFavoritesClick = {
                        viewModel.selectedItemState.value = BottomMenuItem.Favourites.title
                        viewModel.getFavoritesBooks()
                    }
                )
            }
        ) { paddingValue ->
            if (viewModel.isEmptyListState.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                )  {
                    Text("Empty list", color = Color.White)
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(paddingValue)
            ) {
                items(viewModel.booksList.value) { book ->
                    BookListItem(
                        isAdminState.value,
                        book,
                        onEditBook = {
                            onBookEditClick(book)
                        },
                        onDeleteBook = {
                            viewModel.deleteBook(book)
                        },
                        onFavoriteClick = {
                            viewModel.onFavoriteClick(book)
                        },
                        onBookClick = {
                            onBookClick(book)
                        }
                    )
                }
            }
        }
    }
}

fun isAdmin(onAdmin: (Boolean) -> Unit) {
    val uid = com.google.firebase.ktx.Firebase.auth.currentUser!!.uid

    com.google.firebase.ktx.Firebase.firestore.collection("admin")
        .document(uid).get().addOnSuccessListener {
            Log.d("MyLog", "is admin: ${it.get("isAdmin")}")

            onAdmin(it.get("isAdmin") as Boolean)
        }
}