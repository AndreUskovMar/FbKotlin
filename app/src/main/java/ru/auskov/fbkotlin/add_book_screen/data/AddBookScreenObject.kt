package ru.auskov.fbkotlin.add_book_screen.data

import kotlinx.serialization.Serializable
import ru.auskov.fbkotlin.main.utils.Categories

@Serializable
data class AddBookScreenObject (
    val key: String = "",
    val name: String = "",
    val description: String = "",
    val categoryIndex: Int = Categories.FANTASY,
    val price: String = "",
    val imageUrl: String = ""
)