package ru.auskov.fbkotlin.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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
    val selectedCategoryState = mutableStateOf("Favorites")
    val isShowDeleteAlertDialog = mutableStateOf(false)

    var bookToDelete: Book? = null

    private val _uiState = MutableSharedFlow<MainUIState>()

    val uiState = _uiState.asSharedFlow()

    private fun sendUIState(state: MainUIState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun getAllBooks(category: String) {
        selectedCategoryState.value = category
        sendUIState(MainUIState.Loading)
        firestoreManager.getBooksList(
            category,
            onChangeState = { books ->
                isEmptyListState.value = books.isEmpty()
                booksList.value = books
                sendUIState(MainUIState.Success)
            },
            onFailure = { message ->
                sendUIState(MainUIState.Error(message))
            }
        )
    }

    fun getFavoritesBooks() {
        selectedCategoryState.value = "Favorites"
        sendUIState(MainUIState.Loading)
        firestoreManager.getFavoritesBooksList (
            onChangeState = { books ->
                isEmptyListState.value = books.isEmpty()
                booksList.value = books
                sendUIState(MainUIState.Success)
            },
            onFailure = { message ->
                sendUIState(MainUIState.Error(message))
            }
        )
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

    fun deleteBook() {
        bookToDelete?.let { book ->
            firestoreManager.deleteBook(
                book,
                onSuccess = {
                    booksList.value = booksList.value.filter { it -> it.key != book.key }
                    isShowDeleteAlertDialog.value = false
                    bookToDelete = null
                },
                onFailure = { message ->
                    sendUIState(MainUIState.Error(message))
                }
            )
        }
    }

    sealed class MainUIState {
        data object Loading: MainUIState()
        data object Success: MainUIState()
        data class Error(val message: String): MainUIState()
    }
}