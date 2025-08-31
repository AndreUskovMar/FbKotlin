package ru.auskov.fbkotlin.comments_screen

import kotlinx.serialization.Serializable

@Serializable
data class CommentsNavData(
    val id: String = "",
    val title: String = "",
    val ratingList: List<Int> = emptyList(),
)
