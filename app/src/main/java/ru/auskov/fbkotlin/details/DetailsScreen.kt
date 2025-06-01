package ru.auskov.fbkotlin.details

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.Bitmap
import coil3.compose.AsyncImage
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.components.RoundedButton
import ru.auskov.fbkotlin.details.data.DetailsScreenObject

@Composable
fun DetailsScreen(
    navData: DetailsScreenObject = DetailsScreenObject()
) {
    var bitmap: Bitmap? = null

    try {
        val base64Image = Base64.decode(navData.imageUrl, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(base64Image, 0, base64Image.size)
    } catch (e: IllegalArgumentException) {
        Log.d("MyLog", e.message.toString())
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

                    Text(
                        text = stringResource(R.string.creaing_year),
                        color = Color.Gray
                    )
                    Text(
                        text = "2019",
                        fontWeight = FontWeight.Bold
                    )
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

            Text(
                text = navData.description,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
            )
        }

        RoundedButton(
            name = "${stringResource(R.string.creaing_year)} ${navData.price}"
        ) { }
    }
}