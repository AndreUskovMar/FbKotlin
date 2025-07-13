package ru.auskov.fbkotlin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PriceSlider(
    title: String = "Min:",
    value: Float,
    onValueChange: (value: Float) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "$title ${value.toInt()}")

        Slider(
            value = value,
            onValueChange = { value ->
                onValueChange(value)
            },
            steps = 99,
            valueRange = 0f..10000f,
        )
    }
}