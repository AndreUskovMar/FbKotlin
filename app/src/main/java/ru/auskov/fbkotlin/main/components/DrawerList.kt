package ru.auskov.fbkotlin.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.ui.theme.LightGreen

@Composable
fun DrawerList(
    isAdminState: Boolean,
    onAdminClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val listItems = listOf(
        "Favourites",
        "Fantasy",
        "Drama",
        "Bestsellers"
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Image(
            painter = painterResource(R.drawable.login_bg),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f,
            contentScale = ContentScale.Crop
        )

        Column {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(listItems) { item ->
                    DrawerListItem(name = item) {
                        if (item == listItems[0]) {
                            onFavoritesClick()
                        } else {
                            onCategoryClick(item)
                        }
                    }
                }
            }

            if (isAdminState) Button(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(LightGreen),
                onClick = {
                    onAdminClick()
                }
            ) {
                Text("Admin Panel", color = Color.Blue)
            }
        }
    }
}