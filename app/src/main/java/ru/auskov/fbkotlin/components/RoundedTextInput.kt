package ru.auskov.fbkotlin.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.ui.theme.LightGreen

@Composable
fun RoundedTextInput(
    label: String,
    value: String,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    TextField(
        value = value, onValueChange = {
            onValueChange(it)
        },
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, LightGreen, RoundedCornerShape(25.dp)),
        label = {
            Text(text = label, color = Color.Gray)
        },
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = if (isPasswordVisible || !isPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            if (isPassword) {
                IconToggleButton(checked = isPasswordVisible, onCheckedChange = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    Icon(if (isPasswordVisible) {
                        painterResource(R.drawable.visibility_off)
                    } else {
                        painterResource(R.drawable.visibility)
                    }, contentDescription = "visibility_icon")
                }
            }
        }
    )
}