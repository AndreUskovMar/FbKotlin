package ru.auskov.fbkotlin.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.components.CustomAlertDialog
import ru.auskov.fbkotlin.components.CustomFilterDialog
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.login.data.MainScreenDataObject
import ru.auskov.fbkotlin.main.components.BookListItem
import ru.auskov.fbkotlin.main.components.BottomMenu
import ru.auskov.fbkotlin.main.components.BottomMenuItem
import ru.auskov.fbkotlin.main.components.DrawerHeader
import ru.auskov.fbkotlin.main.components.DrawerList
import ru.auskov.fbkotlin.main.components.MainTopBar
import ru.auskov.fbkotlin.main.utils.Categories
import ru.auskov.fbkotlin.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    navData: MainScreenDataObject,
    onBookClick: (Book) -> Unit,
    onBookEditClick: (Book) -> Unit,
    onCloseAccountClick: () -> Unit,
    onAdminClick: () -> Unit,
) {
    val context = LocalContext.current

    val books = viewModel.books.collectAsLazyPagingItems()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val isAdminState = remember {
        mutableStateOf(false)
    }

    var isShownFilterDialog by remember {
        mutableStateOf(false)
    }

    val state = rememberPullToRefreshState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                books.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        isAdmin { isAdmin ->
            isAdminState.value = isAdmin
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiState.collect { uiState ->
            if (uiState is MainScreenViewModel.MainUIState.Error) {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(books.loadState.refresh) {
        if (books.loadState.refresh is LoadState.Error) {
            val errorMessage = (books.loadState.refresh as LoadState.Error).error.message
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(navData.email)
                DrawerList(isAdminState.value, onAdminClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }

                    onAdminClick()

                    // viewModel.booksList.value = emptyList()
                }, onCategoryClick = { categoryIndex ->
                    coroutineScope.launch {
                        drawerState.close()
                    }

                    books.refresh()

                    viewModel.selectedItemState.intValue =
                        if (categoryIndex == Categories.FAVORITES) BottomMenuItem.Favourites.titleId else BottomMenuItem.Home.titleId

                    viewModel.getBooksByCategory(categoryIndex)
                })
            }
        },
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            MainTopBar(viewModel.selectedCategoryState.intValue, onSearch = { text ->
                viewModel.searchBooksByText(text)
                books.refresh()
            }, onFilter = {
                isShownFilterDialog = true
            })
        }, bottomBar = {
            BottomMenu(selectedItem = viewModel.selectedItemState.intValue, onHomeClick = {
                viewModel.selectedItemState.intValue = BottomMenuItem.Home.titleId
                viewModel.getBooksByCategory(Categories.FANTASY)
                books.refresh()
            }, onFavoritesClick = {
                viewModel.selectedItemState.intValue = BottomMenuItem.Favourites.titleId
                viewModel.getBooksByCategory(Categories.FAVORITES)
                books.refresh()
            }, onSettingsClick = {
                viewModel.selectedItemState.intValue = BottomMenuItem.Settings.titleId
            })
        }) { paddingValue ->
            if (viewModel.selectedItemState.intValue == BottomMenuItem.Settings.titleId) {
                Column(
                    modifier = Modifier.padding(paddingValue),
                ) {
                    SettingsScreen(
                        onCloseAccountClick = {
                            onCloseAccountClick()
                        }
                    )
                }

                return@Scaffold
            }

            if (books.itemCount == 0 && books.loadState.refresh is LoadState.NotLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.empty_list), color = Color.White)
                }
            }

            CustomAlertDialog(
                title = stringResource(R.string.delete_book),
                message = stringResource(R.string.sure_delete_book),
                isCancelable = true,
                isShownDialog = viewModel.isShowDeleteAlertDialog.value,
                onConfirm = {
                    viewModel.deleteBook(books.itemSnapshotList.items)
                },
                onDismiss = {
                    viewModel.isShowDeleteAlertDialog.value = false
                    viewModel.bookToDelete = null
                })

            PullToRefreshBox(
                isRefreshing = books.loadState.refresh is LoadState.Loading,
                onRefresh = {
                    books.refresh()
                },
                modifier = Modifier.padding(paddingValue),
                state = state,
                indicator = {
                    if (books.loadState.refresh is LoadState.Loading) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(70.dp), color = Color.Green
                            )
                        }
                    }
                }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(books.itemCount) { bookIndex ->
                        val book = books[bookIndex]
                        book?.let {
                            BookListItem(isAdminState.value, it, onEditBook = {
                                onBookEditClick(book)
                                //viewModel.booksList.value = emptyList()
                            }, onDeleteBook = {
                                viewModel.isShowDeleteAlertDialog.value = true
                                viewModel.bookToDelete = book
                            }, onFavoriteClick = {
                                viewModel.onFavoriteClick(book, books.itemSnapshotList.items)
                            }, onBookClick = {
                                onBookClick(book)
                            })
                        }
                    }
                }
            }

            CustomFilterDialog(
                isShownDialog = isShownFilterDialog,
                title = stringResource(R.string.order_by),
                onConfirm = {
                    viewModel.setFilter()
                    isShownFilterDialog = false
                    books.refresh()
                },
                onDismiss = {
                    isShownFilterDialog = false
                },
            )
        }
    }
}

fun isAdmin(onAdmin: (Boolean) -> Unit) {
    val uid = com.google.firebase.Firebase.auth.currentUser!!.uid

    com.google.firebase.Firebase.firestore.collection("admin")
        .document(uid).get().addOnSuccessListener {
            Log.d("MyLog", "is admin: ${it.get("isAdmin")}")

            onAdmin(it.get("isAdmin") as Boolean)
        }
}