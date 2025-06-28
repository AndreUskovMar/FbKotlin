package ru.auskov.fbkotlin.utils

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import coil3.Bitmap

object ImageUtils {
    fun convertImageToBase64(uri: Uri, contentResolver: ContentResolver): String {
        val inputStream = contentResolver.openInputStream(uri)

        val bytes = inputStream?.readBytes()

        return bytes?.let {
            Base64.encodeToString(it, Base64.DEFAULT)
        } ?: ""
    }
}

fun String.toBitmap(): Bitmap? {
    return try {
        val base64Image = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(base64Image, 0, base64Image.size)
    } catch (e: IllegalArgumentException) {
        Log.d("MyLog", e.message.toString())
        null
    }
}