package ru.auskov.fbkotlin.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.settings.data.MenuItem

@Composable
fun MenuUiItem(item: MenuItem = MenuItem(title = "Something title")) {
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.title)
        if (item.isSwitchVisible) {
            Switch(
                checked = item.isChecked,
                onCheckedChange = {}
            )
        }
    }
}