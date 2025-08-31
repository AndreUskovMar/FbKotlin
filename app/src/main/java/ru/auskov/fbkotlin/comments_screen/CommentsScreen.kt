package ru.auskov.fbkotlin.comments_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.auskov.fbkotlin.details.DetailsScreenViewModel
import ru.auskov.fbkotlin.details.components.CommentsListItem

@SuppressLint("DefaultLocale")
@Composable
fun CommentsScreen(
    navData: CommentsNavData = CommentsNavData(),
    viewModel: DetailsScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getBookComments(navData.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = navData.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (navData.ratingList.isEmpty()) {
                        "-- (0)"
                    } else {
                        String.format(
                            "%.1f",
                            navData.ratingList.average()
                        ) + " (${navData.ratingList.size})"
                    },
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    Icons.Default.Star,
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Star",
                    tint = Color.Yellow
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            LazyColumn {
                items(viewModel.commentsState.value) { item ->
                    CommentsListItem(
                        modifier = Modifier.fillMaxWidth(),
                        item = item
                    ) {}
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }
    }
}