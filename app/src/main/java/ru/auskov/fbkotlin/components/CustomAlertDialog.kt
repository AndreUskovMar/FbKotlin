package ru.auskov.fbkotlin.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomAlertDialog(
    isShownDialog: Boolean,
    title: String = "Reset password",
    message: String = "",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmButtonText: String = "OK"
) {
    if (isShownDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                }, colors = ButtonDefaults.buttonColors(Color.White)) {
                    Text(
                        text = confirmButtonText,
                        color = Color.Blue,
                        fontSize = 16.sp
                    )
                }
            },
            title = {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            },
            containerColor = Color.White,
            tonalElevation = 2.dp
        )
    }
}