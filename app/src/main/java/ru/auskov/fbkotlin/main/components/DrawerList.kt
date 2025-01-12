package ru.auskov.fbkotlin.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ru.auskov.fbkotlin.R

@Composable
fun DrawerList() {
    val listItems = listOf(
        "Favourites",
        "Fantasy",
        "Drama",
        "Bestsellers"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.login_bg),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            alpha = 0.2f,
            contentScale = ContentScale.Crop
        )

        Column {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(listItems) { item ->
                    DrawerListItem(name = item) {

                    }
                }
            }
        }
    }
}