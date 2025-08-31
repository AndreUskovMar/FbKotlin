package ru.auskov.fbkotlin.settings.components

import ru.auskov.fbkotlin.settings.data.MenuItem

object MenuListItem {
    val menuItemsList = listOf(
        MenuItem(
            title = "Account Settings"
        ),
        MenuItem(
            title = "Change Password",
            isCategory = false,
            isSwitchVisible = true
        ),
        MenuItem(
            title = "Change Yor Name",
            isCategory = false
        ),
        MenuItem(
            title = "Change Address",
            isCategory = false
        ),
    )
}