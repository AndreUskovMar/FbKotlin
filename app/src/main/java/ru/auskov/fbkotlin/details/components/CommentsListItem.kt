package ru.auskov.fbkotlin.details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.auskov.fbkotlin.components.StarsIndicator
import ru.auskov.fbkotlin.details.data.RatingData

@Composable
fun CommentsListItem(
    item: RatingData,
    onPress: () -> Unit
) {
    Card(
        modifier = Modifier.width(310.dp).height(150.dp).clickable{
            onPress()
        },
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            StarsIndicator(item.rating)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = item.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = item.message, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}