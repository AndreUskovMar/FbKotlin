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
import com.google.firebase.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.launch
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.data.Favorite
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.main.components.BookListItem
import ru.auskov.fbkotlin.main.components.BottomMenu
import ru.auskov.fbkotlin.main.components.BottomMenuItem
import ru.auskov.fbkotlin.main.components.DrawerHeader
import ru.auskov.fbkotlin.main.components.DrawerList

@Composable
fun MainScreen(
    navData: MainScreenDataObject,
    onBookEditClick: (Book) -> Unit,
    onAdminClick: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val booksList = remember {
        mutableStateOf(emptyList<Book>())
    }

    val coroutineScope = rememberCoroutineScope()
    val selectedItemState = remember { mutableStateOf(BottomMenuItem.Home.title) }

    val isAdminState = remember {
        mutableStateOf(false)
    }

    val isEmptyListState = remember {
        mutableStateOf(false)
    }

    val db = remember {
        Firebase.firestore
    }

    fun getAllBooks(category: String) {
        getFavoriteIdsList(db, navData.uid) { listIds ->
            getBooksList(db, listIds, category) { books ->
                isEmptyListState.value = books.isEmpty()
                booksList.value = books
            }
        }
    }

    fun getFavoritesBooks() {
        getFavoriteIdsList(db, navData.uid) { listIds ->
            getFavoritesBooksList(db, listIds) { books ->
                isEmptyListState.value = books.isEmpty()
                booksList.value = books
            }
        }
    }

    LaunchedEffect(Unit) {
        isAdmin { isAdmin ->
            isAdminState.value = isAdmin
        }
    }

    LaunchedEffect(Unit) {
        getAllBooks("Fantasy")
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

                        selectedItemState.value = BottomMenuItem.Favourites.title

                        getFavoritesBooks()
                    },
                    onCategoryClick = {category ->
                        coroutineScope.launch {
                            drawerState.close()
                        }

                        getAllBooks(category)
                    }
                )
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    selectedItem = selectedItemState.value,
                    onHomeClick = {
                        selectedItemState.value = BottomMenuItem.Home.title
                        getAllBooks("Fantasy")
                    },
                    onFavoritesClick = {
                        selectedItemState.value = BottomMenuItem.Favourites.title
                        getFavoritesBooks()
                    }
                )
            }
        ) { paddingValue ->
            if (isEmptyListState.value) {
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
                items(booksList.value) { book ->
                    BookListItem(
                        isAdminState.value,
                        book,
                        onEditBook = {
                            onBookEditClick(book)
                        },
                        onFavoriteClick = {
                            booksList.value = booksList.value.map {
                                if (book.key == it.key) {
                                    addToFavorites(
                                        db,
                                        Favorite(it.key),
                                        !it.isFavorite,
                                        navData.uid
                                    )

                                    it.copy(isFavorite = !it.isFavorite)
                                } else {
                                    it
                                }

                            }

                            if (selectedItemState.value == BottomMenuItem.Favourites.title) {
                                booksList.value = booksList.value.filter {it.isFavorite}
                                isEmptyListState.value = booksList.value.isEmpty()
                            }
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

private fun getBooksList(
    db: FirebaseFirestore,
    listIds: List<String>,
    category: String,
    onChangeState: (List<Book>) -> Unit
) {
    db.collection("books")
        .whereEqualTo("category", category)
        .get()
        .addOnSuccessListener { task ->
            val booksList = task.toObjects(Book::class.java).map { book ->
                if (listIds.contains(book.key)) {
                    book.copy(isFavorite = true)
                } else {
                    book
                }
            }
            onChangeState(booksList)
        }
        .addOnFailureListener { error ->
            Log.d("MyLog", error.message.toString())
        }
}

private fun getFavoritesBooksList(
    db: FirebaseFirestore,
    listIds: List<String>,
    onChangeState: (List<Book>) -> Unit
) {
    if (listIds.isNotEmpty()) {
        db.collection("books")
            .whereIn(FieldPath.documentId(), listIds)
            .get()
            .addOnSuccessListener { task ->
                val booksList = task.toObjects(Book::class.java).map { book ->
                    book.copy(isFavorite = true)
                }
                onChangeState(booksList)
            }
            .addOnFailureListener { error ->
                Log.d("MyLog", error.message.toString())
            }
    } else {
        onChangeState(emptyList())
    }

}

private fun getFavoriteIdsList(
    db: FirebaseFirestore,
    uid: String,
    onSuccess: (List<String>) -> Unit
) {
    db.collection("users")
        .document(uid)
        .collection("favorites")
        .get()
        .addOnSuccessListener { task ->
            val idsList = arrayListOf<String>()
            val favsList = task.toObjects(Favorite::class.java)
            favsList.forEach {
                idsList.add(it.key)
            }
            onSuccess(idsList)
        }
        .addOnFailureListener { error ->
            Log.d("MyLog", error.message.toString())
        }
}

private fun addToFavorites(
    db: FirebaseFirestore,
    favorite: Favorite,
    isFav: Boolean,
    uid: String,
) {
    if (isFav) {
        db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(favorite.key)
            .set(favorite)
    } else {
        db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(favorite.key)
            .delete()

    }
}