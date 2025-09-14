package ru.auskov.fbkotlin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.auskov.fbkotlin.data.AccountDialog
import ru.auskov.fbkotlin.data.DialogType

@Composable
fun CustomAccountsDialog(
    dialogData: AccountDialog,
    onConfirm: (List<String>, dialogType: DialogType) -> Unit,
    onDismiss: () -> Unit,
) {
    if (dialogData.isShownDialog) {
        val fieldLabelsList = remember {
            mutableStateListOf(*Array(dialogData.fieldLabels.size){""})
        }

        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm(fieldLabelsList, dialogData.dialogType)
                }, colors = ButtonDefaults.buttonColors(Color.White)) {
                    Text(
                        text = "OK",
                        color = Color.Blue,
                        fontSize = 16.sp
                    )
                }

                Button(onClick = {
                    onDismiss()
                }, colors = ButtonDefaults.buttonColors(Color.White)) {
                    Text(
                        text = "Cancel",
                        color = Color.Blue,
                        fontSize = 16.sp
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(dialogData.title),
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    dialogData.fieldLabels.forEachIndexed { index, label ->
                        TextField(
                            value = fieldLabelsList[index],
                            label = {
                                Text(text = label)
                            },
                            onValueChange = { text ->
                                fieldLabelsList[index] = text
                            }
                        )
                    }
                }
            },
            containerColor = Color.White,
            tonalElevation = 2.dp
        )
    }
}