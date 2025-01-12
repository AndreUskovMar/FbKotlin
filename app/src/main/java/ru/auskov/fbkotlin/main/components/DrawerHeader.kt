package ru.auskov.fbkotlin.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.ui.theme.Purple40

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.Green)
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(R.drawable.book),
            contentDescription = "Drawer Logo"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Dogs by Nature",
            fontSize = 25.sp,
            color = Purple40,
            fontWeight = FontWeight.Bold
        )
    }
}