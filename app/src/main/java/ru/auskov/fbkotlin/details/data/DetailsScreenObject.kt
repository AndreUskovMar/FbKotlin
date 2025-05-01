package ru.auskov.fbkotlin.details.data

import kotlinx.serialization.Serializable

@Serializable
data class DetailsScreenObject(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: String = ""
)
