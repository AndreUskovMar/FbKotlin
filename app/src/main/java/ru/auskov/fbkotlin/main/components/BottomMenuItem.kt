package ru.auskov.fbkotlin.main.components

import ru.auskov.fbkotlin.R

sealed class BottomMenuItem(
    val route: String,
    val titleId: Int,
    val iconId: Int
) {
    data object Home: BottomMenuItem(
        route = "",
        titleId = R.string.home,
        iconId = R.drawable.ic_home
    )
    data object Favourites: BottomMenuItem(
        route = "",
        titleId = R.string.favourites,
        iconId = R.drawable.ic_favorite
    )
    data object Settings: BottomMenuItem(
        route = "",
        titleId = R.string.settings,
        iconId = R.drawable.ic_settings
    )
}