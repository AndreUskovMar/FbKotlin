package ru.auskov.fbkotlin.main

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.main.components.BottomMenuItem
import ru.auskov.fbkotlin.main.utils.Categories
import ru.auskov.fbkotlin.utils.firebase.FirestoreManagerPaging

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firestoreManager: FirestoreManagerPaging,
    pager: Flow<PagingData<Book>>
) : ViewModel() {
//    val isEmptyListState = mutableStateOf(false)

    val minPrice = mutableFloatStateOf(0f)
    val maxPrice = mutableFloatStateOf(0f)

    val isFilterByPrice = mutableStateOf(false)

    val selectedItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val selectedCategoryState = mutableIntStateOf(Categories.FANTASY)
    val isShowDeleteAlertDialog = mutableStateOf(false)
    var isDeleteBook = false

    private val mutableBooksList = MutableStateFlow<List<Book>>(emptyList())
    val books: Flow<PagingData<Book>> =
        pager.cachedIn(viewModelScope).combine(mutableBooksList) { pagingData, booksList ->
            val pgData = pagingData.map { book ->
                val updatedBook = booksList.find {
                    book.key == it.key
                }

                updatedBook ?: book
            }

            if (isDeleteBook) {
                isDeleteBook = false
                pgData.filter { book ->
                    booksList.find {
                        book.key == it.key
                    } != null
                }
            } else {
                pgData
            }
        }

    var bookToDelete: Book? = null

    private val _uiState = MutableSharedFlow<MainUIState>()

    val uiState = _uiState.asSharedFlow()

    private fun sendUIState(state: MainUIState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun searchBooksByText(text: String) {
        firestoreManager.searchText = text
    }

    fun getBooksByCategory(categoryIndex: Int) {
        selectedCategoryState.intValue = categoryIndex
        firestoreManager.categoryIndex = categoryIndex
    }

    fun onSavePriceRange() {
        firestoreManager.minPrice = minPrice.floatValue.toInt()
        firestoreManager.maxPrice = maxPrice.floatValue.toInt()
    }

    fun setIsPriceFilterType(isPriceType: Boolean) {
        firestoreManager.isPriceFilterType = isPriceType
    }

    fun onFavoriteClick(book: Book, bookList: List<Book>) {
        val updatedBooksList = firestoreManager.changeFavoriteState(bookList, book)

        mutableBooksList.value =
            if (selectedItemState.intValue == BottomMenuItem.Favourites.titleId) {
                isDeleteBook = true
                updatedBooksList.filter { it.isFavorite }
            } else {
                updatedBooksList
            }
    }

    fun deleteBook(booksList: List<Book>) {
        bookToDelete?.let { book ->
            firestoreManager.deleteBook(
                book,
                onSuccess = {
                    isDeleteBook = true
                    mutableBooksList.value = booksList.filter { it -> it.key != book.key }
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
        data object Loading : MainUIState()
        data object Success : MainUIState()
        data class Error(val message: String) : MainUIState()
    }
}