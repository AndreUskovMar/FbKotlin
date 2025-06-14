package ru.auskov.fbkotlin.main

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.main.components.BottomMenuItem
import ru.auskov.fbkotlin.main.utils.Categories
import ru.auskov.fbkotlin.utils.firebase.FirestoreManager

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firestoreManager: FirestoreManager,
    pager: Flow<PagingData<Book>>
): ViewModel() {
    val booksList = mutableStateOf(emptyList<Book>())
    val isEmptyListState = mutableStateOf(false)
    val selectedItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val selectedCategoryState = mutableIntStateOf(Categories.FAVORITES)
    val isShowDeleteAlertDialog = mutableStateOf(false)

    val books: Flow<PagingData<Book>> = pager.cachedIn(viewModelScope)

    var bookToDelete: Book? = null

    private val _uiState = MutableSharedFlow<MainUIState>()

    val uiState = _uiState.asSharedFlow()

    private fun sendUIState(state: MainUIState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun getAllBooks(categoryIndex: Int) {
        selectedCategoryState.intValue = categoryIndex
        sendUIState(MainUIState.Loading)
        firestoreManager.getBooksList(
            categoryIndex,
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
        selectedCategoryState.intValue = Categories.FAVORITES
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

        booksList.value = if (selectedItemState.intValue == BottomMenuItem.Favourites.titleId) {
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