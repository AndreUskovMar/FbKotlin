package ru.auskov.fbkotlin.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.main.components.BottomMenuItem
import ru.auskov.fbkotlin.utils.firebase.FirestoreManager

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firestoreManager: FirestoreManager
): ViewModel() {
    val booksList = mutableStateOf(emptyList<Book>())
    val isEmptyListState = mutableStateOf(false)
    val selectedItemState = mutableStateOf(BottomMenuItem.Home.title)

    fun getAllBooks(category: String) {
        firestoreManager.getBooksList(category) { books ->
            isEmptyListState.value = books.isEmpty()
            booksList.value = books
        }
    }

    fun getFavoritesBooks() {
        firestoreManager.getFavoritesBooksList { books ->
            isEmptyListState.value = books.isEmpty()
            booksList.value = books
        }
    }

    fun onFavoriteClick(book: Book) {
        val updatedBooksList = firestoreManager.changeFavoriteState(booksList.value, book)

        booksList.value = if (selectedItemState.value == BottomMenuItem.Favourites.title) {
            updatedBooksList.filter {it.isFavorite}
        } else {
            updatedBooksList
        }

        isEmptyListState.value = booksList.value.isEmpty()
    }

    fun deleteBook(book: Book) {
        firestoreManager.deleteBook(book) {
            booksList.value = booksList.value.filter { it -> it.key != book.key }
        }
    }
}