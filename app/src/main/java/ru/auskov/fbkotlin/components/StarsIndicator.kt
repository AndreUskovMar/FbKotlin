package ru.auskov.fbkotlin.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarsIndicator(
    rating: Int
) {
    Row {
        repeat (rating) {
            Icon(
                Icons.Default.Star,
                modifier = Modifier.size(20.dp),
                contentDescription = "Star",
                tint = Color.Yellow
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}