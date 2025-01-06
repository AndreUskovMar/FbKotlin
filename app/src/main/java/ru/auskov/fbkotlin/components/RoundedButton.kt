package ru.auskov.fbkotlin.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.auskov.fbkotlin.ui.theme.LightGreen

@Composable
fun RoundedButton(
    name: String,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            LightGreen
        )
    ) {
        Text(name)
    }
}