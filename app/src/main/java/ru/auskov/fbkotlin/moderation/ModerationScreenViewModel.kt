package ru.auskov.fbkotlin.moderation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.auskov.fbkotlin.details.data.RatingData
import ru.auskov.fbkotlin.utils.firebase.FirestoreManagerPaging
import javax.inject.Inject

@HiltViewModel
class ModerationScreenViewModel @Inject constructor(
    private val firestoreManagerPaging: FirestoreManagerPaging
): ViewModel() {
    val commentsState = mutableStateOf(emptyList<RatingData>())

    fun insertBookRating(ratingData: RatingData) {
        firestoreManagerPaging.insertModerationRating(ratingData)
        removeCommentFromModeration(ratingData.uid)
    }

    fun removeCommentFromModeration(uid: String) = viewModelScope.launch{
        firestoreManagerPaging.deleteModerationComment(uid)
        commentsState.value = commentsState.value.filter { comment -> comment.uid != uid }
    }

    fun getComments() = viewModelScope.launch {
        commentsState.value = firestoreManagerPaging.getCommentsToModeration()
    }
}