package ru.auskov.fbkotlin.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.main.utils.Categories
import ru.auskov.fbkotlin.ui.theme.LightGreen

@Composable
fun DrawerList(
    isAdminState: Boolean,
    onAdminClick: () -> Unit,
    onCategoryClick: (Int) -> Unit
) {
    val listItems = stringArrayResource(id = R.array.category_array)

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Image(
            painter = painterResource(R.drawable.login_bg),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f,
            contentScale = ContentScale.Crop
        )

        Column {
            DrawerListItem(name = stringResource(R.string.favourites)) {
                onCategoryClick(Categories.FAVORITES)
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(listItems) { index, item ->
                    DrawerListItem(name = item) {
                        onCategoryClick(index)
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
                Text(stringResource(R.string.admin_panel), color = Color.Blue)
            }
        }
    }
}