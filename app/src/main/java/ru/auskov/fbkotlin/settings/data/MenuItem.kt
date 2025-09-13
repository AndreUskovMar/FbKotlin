package ru.auskov.fbkotlin.settings.data

import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.data.DialogType

sealed class MenuItem {
    data class CategoryItem(val title: Int): MenuItem()
    data class DialogItem(
        val title: Int,
        val dialogType: DialogType = DialogType.PERSONAL_DATA,
        val fieldsLabelsArrayId: Int = R.array.personal_data_array
    ): MenuItem()
    data class DropDownItem(
        val title: Int,
        val menuType: DropDownMenuType = DropDownMenuType.IMAGE_FORMAT,
        val arrayId: Int = R.array.personal_data_array
    ): MenuItem()
}

enum class DropDownMenuType {
    IMAGE_FORMAT,
    IMAGE_QUALITY,
    IMAGE_SIZE
}