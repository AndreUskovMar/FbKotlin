package ru.auskov.fbkotlin.settings.data

import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.data.DialogType

data class MenuItem(
    val title: Int,
    val isCategory: Boolean = false,
    val isChecked: Boolean = false,
    val isSwitchVisible: Boolean = false,
    val dialogType: DialogType = DialogType.PERSONAL_DATA,
    val fieldsLabelsArrayId: Int = R.array.personal_data_array
)