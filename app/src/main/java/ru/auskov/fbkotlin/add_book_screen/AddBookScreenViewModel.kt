package ru.auskov.fbkotlin.add_book_screen

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.auskov.fbkotlin.add_book_screen.data.AddBookScreenObject
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.main.MainScreenViewModel.MainUIState
import ru.auskov.fbkotlin.main.utils.Categories
import ru.auskov.fbkotlin.utils.firebase.FirestoreManagerPaging
import javax.inject.Inject

@HiltViewModel
class AddBookScreenViewModel @Inject constructor(
    private val firestoreManager: FirestoreManagerPaging
) : ViewModel() {
    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val price = mutableStateOf("")
    val categoryIndex = mutableIntStateOf(Categories.FANTASY)
    val imageUri = mutableStateOf<Uri?>(null)

    private val _uiState = MutableSharedFlow<MainUIState>()

    val uiState = _uiState.asSharedFlow()

    private fun sendUIState(state: MainUIState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun setDefaultsData(navData: AddBookScreenObject) {
        title.value = navData.name
        description.value = navData.description
        price.value = navData.price
        categoryIndex.intValue = navData.categoryIndex
    }

    fun uploadBook(
        navData: AddBookScreenObject,
        onSuccess: () -> Unit
    ) {
        val book = Book(
            key = navData.key,
            name = title.value,
            description = description.value,
            price = price.value.toInt(),
            categoryIndex = categoryIndex.intValue,
            imageUrl = navData.imageUrl
        )

        sendUIState(MainUIState.Loading)

        firestoreManager.saveBookImage(
            prevImageUrl = navData.imageUrl,
            uri = imageUri.value,
            book = book,
            onSuccess = {
                sendUIState(MainUIState.Success)
                onSuccess()
            },
            onError = { message ->
                sendUIState(MainUIState.Error(message))
            }
        )
    }
}