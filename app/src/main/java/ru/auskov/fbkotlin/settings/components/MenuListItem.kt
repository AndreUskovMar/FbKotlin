package ru.auskov.fbkotlin.settings.components

import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.data.DialogType
import ru.auskov.fbkotlin.settings.data.DropDownMenuType
import ru.auskov.fbkotlin.settings.data.MenuItem

object MenuListItem {
    val menuItemsList = listOf(
        MenuItem.CategoryItem(
            title = R.string.account_settings,
        ),
        MenuItem.DialogItem(
            title = R.string.change_password,
            dialogType = DialogType.PASSWORD,
            fieldsLabelsArrayId = R.array.password_array
        ),
        MenuItem.DialogItem(
            title = R.string.change_name,
            dialogType = DialogType.PERSONAL_DATA,
            fieldsLabelsArrayId = R.array.personal_data_array
        ),
        MenuItem.DialogItem(
            title = R.string.change_address,
            dialogType = DialogType.ADDRESS,
            fieldsLabelsArrayId = R.array.address_array
        ),
        MenuItem.DialogItem(
            title = R.string.delete_account,
            dialogType = DialogType.DELETE_ACCOUNT,
            fieldsLabelsArrayId = R.array.delete_account_array
        ),
        MenuItem.CategoryItem(
            title = R.string.image_settings,
        ),
        MenuItem.DropDownItem(
            title = R.string.image_format,
            arrayId = R.array.image_format_array,
            menuType = DropDownMenuType.IMAGE_FORMAT,
        ),
        MenuItem.DropDownItem(
            title = R.string.image_quality,
            arrayId = R.array.image_quality_array,
            menuType = DropDownMenuType.IMAGE_QUALITY
        ),
        MenuItem.DropDownItem(
            title = R.string.image_size,
            arrayId = R.array.image_size_array,
            menuType = DropDownMenuType.IMAGE_SIZE
        ),
    )
}