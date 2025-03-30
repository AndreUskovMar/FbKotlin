package ru.auskov.fbkotlin.add_book_screen.data

import kotlinx.serialization.Serializable

@Serializable
data class AddBookScreenObject (
    val key: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val price: String = "",
    val imageUrl: String = ""
)