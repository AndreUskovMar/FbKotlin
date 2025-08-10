package ru.auskov.fbkotlin.details.data

data class RatingData(
    val uid: String = "",
    val bookId: String = "",
    val name: String = "",
    val rating: Int = 0,
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
