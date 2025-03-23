package ru.auskov.fbkotlin.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.ui.theme.LightGreen

@Composable
fun RoundedTextInput(
    label: String,
    value: String,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value, onValueChange = {
            onValueChange(it)
        },
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, LightGreen, RoundedCornerShape(25.dp)),
        label = {
            Text(text = label, color = Color.Gray)
        },
        singleLine = singleLine,
        maxLines = maxLines
    )
}