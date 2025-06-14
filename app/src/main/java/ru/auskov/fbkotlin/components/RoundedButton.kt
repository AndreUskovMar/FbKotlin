package ru.auskov.fbkotlin.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.ui.theme.LightGreen

@Composable
fun RoundedButton(
    name: String,
    isLoadingIndicator: Boolean = false,
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
        if (isLoadingIndicator) {
            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                color = Color.Green
            )
        } else {
            Text(name)
        }
    }
}