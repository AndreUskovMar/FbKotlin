package ru.auskov.fbkotlin.moderation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.auskov.fbkotlin.moderation.components.AdminCommentsListItem
import ru.auskov.fbkotlin.ui.theme.Purple80

@Composable
fun ModerationScreen(
    viewModel: ModerationScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getComments()
    }

    Box(
        Modifier.fillMaxSize().background(Purple80),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(vertical = 40.dp, horizontal = 10.dp)
        ) {
            items(viewModel.commentsState.value) { item ->
                AdminCommentsListItem(
                    item = item,
                    onAccept = {
                        viewModel.insertBookRating(item)
                    },
                    onDecline = {
                        viewModel.removeCommentFromModeration(item.uid)
                    }
                )
                Spacer(Modifier.height(10.dp))
            }
        }

        if (viewModel.commentsState.value.isEmpty()) {
            Text(text = "No comments was found", color = Color.Blue)
        }
    }
}