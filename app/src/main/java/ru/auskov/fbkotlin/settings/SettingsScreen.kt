package ru.auskov.fbkotlin.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
// import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.components.CustomAccountsDialog
import ru.auskov.fbkotlin.components.RoundedButton
import ru.auskov.fbkotlin.data.AccountDialog
import ru.auskov.fbkotlin.settings.components.MenuCategoryItem
import ru.auskov.fbkotlin.settings.components.MenuListItem
import ru.auskov.fbkotlin.settings.components.MenuUiItem

// @Preview(showBackground = true)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {}
) {
    var dialogData by remember {
        mutableStateOf(AccountDialog())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    "Андрей Усков",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Spacer(Modifier.height(5.dp))
                Text("andrewuskov2211@gmail.com", color = Color.DarkGray)
                Spacer(Modifier.height(2.dp))
                Text("287541, Мариуполь, ул. Азовстальская, д.43/6", color = Color.DarkGray)
                Spacer(Modifier.height(2.dp))
                Text("+79496354001", color = Color.Blue)
                Spacer(Modifier.height(2.dp))
                Text("last visit: 30.08.2025", color = Color.DarkGray)
            }
        }

        Spacer(Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                items(MenuListItem.menuItemsList) { item ->
                    if (item.isCategory) {
                        MenuCategoryItem(stringResource(item.title))
                    } else {
                        MenuUiItem(item) { dialogType, fieldLabelsList ->
                            dialogData = AccountDialog(
                                title = item.title,
                                isShownDialog = true,
                                dialogType = dialogType,
                                fieldLabels = fieldLabelsList
                            )
                        }
                    }
                }
            }
        }

        RoundedButton(
            name = stringResource(R.string.save),
            modifier = Modifier.fillMaxWidth()
        ) { }

        RoundedButton(
            name = stringResource(R.string.back),
            modifier = Modifier.fillMaxWidth()
        ) {
            onBackClick()
        }

        CustomAccountsDialog(
            dialogData = dialogData,
            onConfirm = { listFieldsValues ->
                dialogData = AccountDialog(isShownDialog = false)
            },
            onDismiss = {
                dialogData = AccountDialog(isShownDialog = false)
            }
        )
    }
}