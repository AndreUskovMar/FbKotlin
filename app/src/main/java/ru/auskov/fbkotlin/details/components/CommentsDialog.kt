package ru.auskov.fbkotlin.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.auskov.fbkotlin.components.StarsIndicator
import ru.auskov.fbkotlin.details.data.RatingData

@Composable
fun CommentsDialog(
    isShownDialog: Boolean = true,
    ratingData: RatingData = RatingData(),
    onDismiss: () -> Unit = {},
    confirmButtonText: String = "OK",
) {
    if (isShownDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                Button(onClick = {
                    onDismiss()
                }, colors = ButtonDefaults.buttonColors(Color.White)) {
                    Text(
                        text = confirmButtonText,
                        color = Color.Blue,
                        fontSize = 16.sp
                    )
                }
            },
            title = {
                Column {
                    StarsIndicator(ratingData.rating)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = ratingData.name,
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Text(
                    text = ratingData.message,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            },
            containerColor = Color.White,
            tonalElevation = 2.dp
        )
    }
}