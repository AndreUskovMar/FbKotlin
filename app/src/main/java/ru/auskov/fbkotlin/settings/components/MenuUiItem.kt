package ru.auskov.fbkotlin.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.data.DialogType
import ru.auskov.fbkotlin.settings.data.MenuItem

@Composable
fun MenuUiItem(
    item: MenuItem = MenuItem(title = R.string.account_settings),
    onItemClick: (DialogType, fieldLabels: List<String>) -> Unit
) {
    val fieldLabelsList = stringArrayResource(item.fieldsLabelsArrayId).toList()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp).clickable {
                onItemClick(item.dialogType, fieldLabelsList)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(item.title))
        if (item.isSwitchVisible) {
            Switch(
                checked = item.isChecked,
                onCheckedChange = {}
            )
        }
    }
}