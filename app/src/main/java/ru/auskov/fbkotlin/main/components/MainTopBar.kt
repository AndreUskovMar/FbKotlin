package ru.auskov.fbkotlin.main.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.main.utils.Categories
import ru.auskov.fbkotlin.ui.theme.Pink80
import ru.auskov.fbkotlin.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    titleId: Int,
    onSearch: (String) -> Unit,
    onFilter: () -> Unit,
) {
    var targetState by remember {
        mutableStateOf(false)
    }

    var expandedState by remember {
        mutableStateOf(false)
    }

    var inputText by remember {
        mutableStateOf("")
    }

    Crossfade(targetState = targetState) { target ->
        if (target) {
            SearchBar(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = inputText,
                        placeholder = {
                            Text(text = stringResource(R.string.start))
                        },
                        onQueryChange = { text ->
                            inputText = text
                        },
                        onSearch = { text ->
                            onSearch(text)
                        },
                        expanded = expandedState,
                        onExpandedChange = {
                            // expandedState = it
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    expandedState = false
                                    targetState = false
                                    inputText = ""
                                    onSearch("")
                                }
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTrailingIconColor = Purple40,
                            unfocusedTrailingIconColor = Purple40,
                            focusedIndicatorColor = Purple40,
                            unfocusedIndicatorColor = Purple40
                        )
                    )
                },
                expanded = expandedState,
                onExpandedChange = {
                    expandedState = it
                },
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(5) {
                            Text(
                                text = "Text $it",
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            )
        } else {
            TopAppBar(
                title = {
                    Text(text = if (titleId == Categories.FAVORITES)
                        stringResource(R.string.favourites)
                    else
                        stringArrayResource(R.array.category_array)[titleId])
                },
                actions = {
                    IconButton(
                        onClick = {
                            targetState = true
                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(
                        onClick = {
                            onFilter()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Filter")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Pink80,
                    titleContentColor = Purple40,
                    actionIconContentColor = Purple40
                )
            )
        }
    }
}