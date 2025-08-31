package ru.auskov.fbkotlin.settings.data

data class MenuItem(
    val title: String,
    val isCategory: Boolean = true,
    val isChecked: Boolean = false,
    val isSwitchVisible: Boolean = false,
)