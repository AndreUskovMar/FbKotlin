package ru.auskov.fbkotlin.data

import ru.auskov.fbkotlin.main.utils.Categories

data class Book(
    val key: String = "",
    val name: String = "",
    val searchName: String = name.lowercase(),
    val description: String = "",
    val categoryIndex: Int = Categories.FANTASY,
    val price: Int = 0,
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val ratingList: List<Int> = emptyList()
)
