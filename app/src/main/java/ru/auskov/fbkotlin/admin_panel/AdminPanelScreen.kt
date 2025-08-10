package ru.auskov.fbkotlin.admin_panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.components.RoundedButton

@Composable
fun AdminPanelScreen(
    onAddBookClick: () -> Unit,
    onModerationClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RoundedButton(
            name = stringResource(R.string.add_new_book),
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            onAddBookClick()
        }
        RoundedButton(
            name = stringResource(R.string.comment_moderation),
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            onModerationClick()
        }
    }
}