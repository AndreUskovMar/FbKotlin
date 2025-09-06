package ru.auskov.fbkotlin.settings.components

import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.data.DialogType
import ru.auskov.fbkotlin.settings.data.MenuItem

object MenuListItem {
    val menuItemsList = listOf(
        MenuItem(
            title = R.string.account_settings,
            isCategory = true,
        ),
        MenuItem(
            title = R.string.change_password,
            dialogType = DialogType.PASSWORD,
            fieldsLabelsArrayId = R.array.password_array
        ),
        MenuItem(
            title = R.string.change_name,
            dialogType = DialogType.PERSONAL_DATA,
            fieldsLabelsArrayId = R.array.personal_data_array
        ),
        MenuItem(
            title = R.string.change_address,
            dialogType = DialogType.ADDRESS,
            fieldsLabelsArrayId = R.array.address_array
        ),
        MenuItem(
            title = R.string.delete_account,
            dialogType = DialogType.DELETE_ACCOUNT,
            fieldsLabelsArrayId = R.array.delete_account_array
        ),
        MenuItem(
            title = R.string.image_settings,
            isCategory = true,
        ),
        MenuItem(
            title = R.string.image_format
        ),
        MenuItem(
            title = R.string.image_quality
        ),
    )
}