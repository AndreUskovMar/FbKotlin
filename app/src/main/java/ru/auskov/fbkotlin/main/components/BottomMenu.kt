package ru.auskov.fbkotlin.main.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.auskov.fbkotlin.ui.theme.Pink80
import ru.auskov.fbkotlin.ui.theme.Purple40
import ru.auskov.fbkotlin.ui.theme.PurpleGrey40

@Composable
fun BottomMenu(
    selectedItem: Int,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
) {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favourites,
        BottomMenuItem.Settings
    )

    NavigationBar(
        containerColor = Pink80
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item.titleId,
                onClick = {
                    when(item.titleId) {
                        BottomMenuItem.Home.titleId -> onHomeClick()
                        BottomMenuItem.Favourites.titleId -> onFavoritesClick()
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = item.titleId.toString()
                    )
                },
                label = {
                    Text(text = stringResource(item.titleId))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Purple40,
                    selectedTextColor = Purple40,
                    indicatorColor = Pink80,
                    unselectedIconColor = PurpleGrey40,
                    unselectedTextColor = PurpleGrey40
                )
            )
        }
    }
}