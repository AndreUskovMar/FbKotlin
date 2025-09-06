package ru.auskov.fbkotlin.data

data class AccountDialog(
    val title: Int = 0,
    val fieldLabels: List<String> = emptyList(),
    val isShownDialog: Boolean = false,
    val dialogType: DialogType = DialogType.PERSONAL_DATA
)

enum class DialogType {
    PERSONAL_DATA,
    PASSWORD,
    ADDRESS,
    DELETE_ACCOUNT
}
