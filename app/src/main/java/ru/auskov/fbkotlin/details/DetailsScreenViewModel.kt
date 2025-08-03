package ru.auskov.fbkotlin.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.auskov.fbkotlin.utils.firebase.FirestoreManagerPaging
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val firestoreManagerPaging: FirestoreManagerPaging
): ViewModel() {
    val bookRating = mutableStateOf("0.0")

    fun insertBookRating(bookId: String, bookRating: Int) {
        firestoreManagerPaging.insertRating(bookId, bookRating)
    }

    fun getBookRating(bookId: String) = viewModelScope.launch {
        bookRating.value = firestoreManagerPaging.getRating(bookId).toString()
    }
}