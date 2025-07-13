package ru.auskov.fbkotlin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RadioButtonsSet(
    selectedOption: Int,
    options: Array<String>,
    onValueChange: (String) -> Unit
) {
    Column (modifier = Modifier.fillMaxWidth()) {
        options.forEach { option ->
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = options[selectedOption] == option,
                    onClick = {
                        onValueChange(option)
                    },
                )
                Text(text = option)
            }
        }
    }
}