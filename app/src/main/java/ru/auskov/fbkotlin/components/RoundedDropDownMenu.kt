package ru.auskov.fbkotlin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.ui.theme.LightGreen

@Composable
fun RoundedDropDownMenu(
    categoryIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    val listItems = stringArrayResource(R.array.category_array)

    val expanded = remember {
        mutableStateOf(false)
    }

    val categoryOption = listItems[categoryIndex]

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, LightGreen, RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
            .clickable {
                expanded.value = true
            }
            .padding(horizontal = 15.dp, vertical = 17.dp)
    ) {
        Text(categoryOption)
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            listItems.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Text(option)
                    },
                    onClick = {
                        // selectedItem.value = option
                        expanded.value = false
                        onOptionSelected(index)
                    }
                )
            }
        }
    }
}