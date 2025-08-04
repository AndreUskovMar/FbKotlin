package ru.auskov.fbkotlin.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.auskov.fbkotlin.R

@Preview(showBackground = true)
@Composable
fun CustomRatingDialog(
    onSubmit: (Int, String) -> Unit = {_, _ ->},
    onDismiss: () -> Unit = {},
    isVisible: Boolean = true
) {
    var selectedRating by remember { mutableIntStateOf(0) }
    var messageState by remember { mutableStateOf("") }

    if (isVisible) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = stringResource(R.string.rate_book),
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (rating in 1..5) {
                            Box(
                                modifier = Modifier.size(40.dp).clickable {
                                    selectedRating = rating
                                }
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    modifier = Modifier.size(40.dp),
                                    contentDescription = "Star",
                                    tint = if (rating <= selectedRating) Color.Yellow else Color.Gray
                                )
                            }
                        }
                    }

                    TextField(
                        value = messageState,
                        onValueChange = { message ->
                            messageState = message
                        },
                        placeholder = {
                            Text(text = stringResource(R.string.comment))
                        },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onSubmit(selectedRating, messageState)
                }, colors = ButtonDefaults.buttonColors(Color.White)) {
                    Text(
                        text = stringResource(R.string.submit),
                        color = Color.Blue,
                        fontSize = 16.sp
                    )
                }
            }
        )
    }
}