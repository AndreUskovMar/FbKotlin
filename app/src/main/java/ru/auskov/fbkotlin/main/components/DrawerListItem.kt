package ru.auskov.fbkotlin.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.auskov.fbkotlin.ui.theme.White30

@Composable
fun DrawerListItem (name: String, onPress: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clickable {
            onPress()
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(White30)
        )
    }
}