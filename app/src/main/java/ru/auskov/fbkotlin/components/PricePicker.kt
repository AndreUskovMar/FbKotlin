package ru.auskov.fbkotlin.components

import android.widget.NumberPicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PricePicker(
    value: Int,
    range: IntRange,
    onChangeValue: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = range.first
                maxValue = range.last
                setOnValueChangedListener { _, _, value ->
                    onChangeValue(value)
                }
                this.value = value
            }
        },
        update = { picker ->
            picker.minValue = range.first
            picker.maxValue = range.last
            if (picker.value != value) {
                picker.value = value
            }
        }
    )
}