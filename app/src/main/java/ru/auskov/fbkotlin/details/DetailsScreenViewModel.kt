package ru.auskov.fbkotlin.details

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.auskov.fbkotlin.details.data.RatingData
import ru.auskov.fbkotlin.utils.firebase.FirestoreManagerPaging
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val firestoreManagerPaging: FirestoreManagerPaging
): ViewModel() {
    val bookRating = mutableStateOf("0.0")
    val commentsState = mutableStateOf(emptyList<RatingData>())
    val ratingDataState = mutableStateOf<RatingData?>(RatingData())

    fun insertBookRating(bookId: String, ratingData: RatingData, context: Context) {
        firestoreManagerPaging.insertRating(bookId, ratingData, context)
    }

    fun getBookRating(bookId: String) = viewModelScope.launch {
        val ratingPair = firestoreManagerPaging.getRating(bookId)
        if (ratingPair.second.isNotEmpty()) {
            bookRating.value = ratingPair.first.toString()
            commentsState.value = ratingPair.second
        }
    }

    fun getUserRating(bookId: String) = viewModelScope.launch {
        ratingDataState.value = firestoreManagerPaging.getUserRating(bookId)
    }
}