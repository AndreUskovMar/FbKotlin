package ru.auskov.fbkotlin.add_book_screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import ru.auskov.fbkotlin.components.RoundedButton
import ru.auskov.fbkotlin.components.RoundedDropDownMenu
import ru.auskov.fbkotlin.components.RoundedTextInput
import ru.auskov.fbkotlin.ui.theme.Purple40
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.auskov.fbkotlin.R
import androidx.compose.ui.platform.LocalContext
import ru.auskov.fbkotlin.add_book_screen.data.AddBookScreenObject
import ru.auskov.fbkotlin.main.MainScreenViewModel
import ru.auskov.fbkotlin.utils.ImageUtils
import ru.auskov.fbkotlin.utils.firebase.IS_BASE64
import ru.auskov.fbkotlin.utils.toBitmap

@Composable
fun AddBookScreen(
    navData: AddBookScreenObject = AddBookScreenObject(),
    onSavedSuccess: () -> Unit = {},
    viewModel: AddBookScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val navImageUrl = remember {
        mutableStateOf(navData.imageUrl)
    }

    val imageBase64 = remember {
        mutableStateOf(if (IS_BASE64) navData.imageUrl else "")
    }

    val isShownIndicator = remember {
        mutableStateOf(false)
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (IS_BASE64) {
            imageBase64.value = uri?.let {
                ImageUtils.convertImageToBase64(it, context.contentResolver)
            } ?: ""
        } else {
            navImageUrl.value = ""
            viewModel.imageUri.value = uri
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setDefaultsData(navData)

        viewModel.uiState.collect { state ->
            when (state) {
                is MainScreenViewModel.MainUIState.Loading -> {
                    isShownIndicator.value = true
                }

                is MainScreenViewModel.MainUIState.Success -> {
                    isShownIndicator.value = false
                }

                is MainScreenViewModel.MainUIState.Error -> {
                    isShownIndicator.value = false
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Image(
        painter = rememberAsyncImagePainter(
            model = if (imageBase64.value.isEmpty()) {
                navImageUrl.value.ifEmpty { viewModel.imageUri.value }
            } else {
                imageBase64.value.toBitmap()
            }
        ),
        contentDescription = "Add Book",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.2f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_title),
            style = TextStyle(
                fontSize = 46.sp,
                lineHeight = 50.sp,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                )
            ),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Purple40,
            modifier = Modifier.padding(bottom = 50.dp),
        )

        RoundedDropDownMenu(
            categoryIndex = viewModel.categoryIndex.intValue,
            onOptionSelected = { selectedCategoryIndex ->
                viewModel.categoryIndex.intValue = selectedCategoryIndex
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        RoundedTextInput(
            label = stringResource(R.string.title),
            value = viewModel.title.value
        ) {
            viewModel.title.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedTextInput(
            label = stringResource(R.string.description),
            value = viewModel.description.value,
            singleLine = false,
            maxLines = 5
        ) {
            viewModel.description.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedTextInput(
            label = stringResource(R.string.price),
            value = viewModel.price.value
        ) {
            viewModel.price.value = it
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RoundedButton(name = stringResource(R.string.add_book_image)) {
                imageLauncher.launch("image/*")
            }

            Spacer(modifier = Modifier.height(10.dp))

            RoundedButton(
                name = stringResource(R.string.save_book),
                isLoadingIndicator = isShownIndicator.value
            ) {
                viewModel.uploadBook(navData.copy(imageUrl = imageBase64.value)) {
                    onSavedSuccess()
                }
            }
        }
    }
}