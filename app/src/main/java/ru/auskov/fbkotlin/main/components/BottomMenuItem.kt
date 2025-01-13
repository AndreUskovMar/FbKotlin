package ru.auskov.fbkotlin.main.components

import ru.auskov.fbkotlin.R

sealed class BottomMenuItem(
    val route: String,
    val title: String,
    val iconId: Int
) {
    data object Home: BottomMenuItem(
        route = "",
        title = "Home",
        iconId = R.drawable.ic_home
    )
    data object Favourites: BottomMenuItem(
        route = "",
        title = "Favourites",
        iconId = R.drawable.ic_favorite
    )
    data object Settings: BottomMenuItem(
        route = "",
        title = "Settings",
        iconId = R.drawable.ic_settings
    )
}