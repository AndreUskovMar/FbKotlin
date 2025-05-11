package ru.auskov.fbkotlin.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.main.components.BottomMenuItem
import ru.auskov.fbkotlin.utils.firebase.FirebaseManager

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firebaseManager: FirebaseManager
): ViewModel() {
    val booksList = mutableStateOf(emptyList<Book>())
    val isEmptyListState = mutableStateOf(false)

    fun getAllBooks(category: String) {
        firebaseManager.getBooksList(category) { books ->
            isEmptyListState.value = books.isEmpty()
            booksList.value = books
        }
    }

    fun getFavoritesBooks() {
        firebaseManager.getFavoritesBooksList { books ->
            isEmptyListState.value = books.isEmpty()
            booksList.value = books
        }
    }

    fun onFavoriteClick(book: Book, tabName: String) {
        val updatedBooksList = firebaseManager.changeFavoriteState(booksList.value, book)

        booksList.value = if (tabName == BottomMenuItem.Favourites.title) {
            updatedBooksList.filter {it.isFavorite}
        } else {
            updatedBooksList
        }

        isEmptyListState.value = booksList.value.isEmpty()
    }
}