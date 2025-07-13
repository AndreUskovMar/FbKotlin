package ru.auskov.fbkotlin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.main.MainScreenViewModel

@Preview(showBackground = true)
@Composable
fun CustomFilterDialog(
    isShownDialog: Boolean = true,
    title: String = stringResource(R.string.order_by),
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    confirmButtonText: String = "OK",
    cancelButtonText: String = "CANCEL",
    isCancelable: Boolean = false,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val orderByList = stringArrayResource(R.array.order_by)

    if (isShownDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                }, colors = ButtonDefaults.buttonColors(Color.White)) {
                    Text(
                        text = confirmButtonText,
                        color = Color.Blue,
                        fontSize = 16.sp
                    )
                }

                if (isCancelable) {
                    Button(onClick = {
                        onDismiss()
                    }, colors = ButtonDefaults.buttonColors(Color.White)) {
                        Text(
                            text = cancelButtonText,
                            color = Color.Blue,
                            fontSize = 16.sp
                        )
                    }
                }
            },
            title = {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    RadioButtonsSet(
                        selectedOption = if (viewModel.isFilterByPrice.value) 1 else 0,
                        options = orderByList
                    ) { option ->
                        viewModel.isFilterByPrice.value = option == orderByList[1]
                        viewModel.setIsPriceFilterType(viewModel.isFilterByPrice.value)
                    }

                    if (viewModel.isFilterByPrice.value) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.price_range),
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                        PriceSlider(
                            title = "${stringResource(R.string.min)}:",
                            value = viewModel.minPrice.floatValue
                        ) { value ->
                            viewModel.minPrice.floatValue = value
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        PriceSlider(
                            title = "${stringResource(R.string.max)}:",
                            value = viewModel.maxPrice.floatValue
                        ) { value ->
                            viewModel.maxPrice.floatValue = value
                        }
                    }
                }
            },
            containerColor = Color.White,
            tonalElevation = 2.dp
        )
    }
}