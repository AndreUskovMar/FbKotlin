package ru.auskov.fbkotlin.details

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.Bitmap
import coil3.compose.AsyncImage
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.components.CustomRatingDialog
import ru.auskov.fbkotlin.components.RoundedButton
import ru.auskov.fbkotlin.details.components.CommentsDialog
import ru.auskov.fbkotlin.details.components.CommentsListItem
import ru.auskov.fbkotlin.details.data.DetailsScreenObject
import ru.auskov.fbkotlin.details.data.RatingData

@SuppressLint("DefaultLocale")
@Composable
fun DetailsScreen(
    navData: DetailsScreenObject = DetailsScreenObject(),
    viewModel: DetailsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var bitmap: Bitmap? = null
    var isVisibleRateDialog by remember {
        mutableStateOf(false)
    }
    var isVisibleCommentsDialog by remember {
        mutableStateOf(false)
    }
    var ratingDataToShow by remember {
        mutableStateOf(RatingData())
    }

    try {
        val base64Image = Base64.decode(navData.imageUrl, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(base64Image, 0, base64Image.size)
    } catch (e: IllegalArgumentException) {
        Log.d("MyLog", e.message.toString())
    }

    LaunchedEffect(key1 = Unit){
        viewModel.getBookRating(navData.id)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 50.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = bitmap ?: navData.imageUrl,
                    contentDescription = navData.title,
                    modifier = Modifier.fillMaxWidth(0.5f)
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.FillHeight
                )

                Spacer(modifier = Modifier.width(15.dp))

                Column(modifier = Modifier.fillMaxWidth()
                    .height(190.dp)
                ) {
                    Text(
                        text = stringResource(R.string.creator),
                        color = Color.Gray
                    )
                    Text(
                        text = "Украинский народ",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.creating_year),
                        color = Color.Gray
                    )
                    Text(
                        text = "2019",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.rating),
                        color = Color.Gray
                    )
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format("%.1f", viewModel.bookRating.value.toDouble()),
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            Icons.Default.Star,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Star",
                            tint = Color.Yellow
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                RoundedButton(
                    name = stringResource(R.string.buy_now),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) { }
                Spacer(modifier = Modifier.width(20.dp))
                RoundedButton(
                    name = stringResource(R.string.rate_book),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    viewModel.getUserRating(navData.id)
                    isVisibleRateDialog = true
                }
            }

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

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = navData.description,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyRow {
                items(viewModel.commentsState.value) { item ->
                    CommentsListItem(item) {
                        ratingDataToShow = item
                        isVisibleCommentsDialog = true
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }

        CommentsDialog(
            isShownDialog = isVisibleCommentsDialog,
            ratingData = ratingDataToShow,
            onDismiss = {
                isVisibleCommentsDialog = false
            }
        )

        CustomRatingDialog(
            isVisible = isVisibleRateDialog,
            ratingData = viewModel.ratingDataState.value ?: RatingData(),
            onSubmit = { rating, message ->
                isVisibleRateDialog = false
                val ratingData = RatingData(
                    name = "",
                    rating = rating,
                    message = message
                )
                viewModel.insertBookRating(bookId = navData.id, ratingData, context)
            },
            onDismiss = {
                isVisibleRateDialog = false
            }
        )
    }
}