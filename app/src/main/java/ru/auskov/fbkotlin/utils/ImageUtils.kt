package ru.auskov.fbkotlin.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun convertImageToBase64(uri: Uri, contentResolver: ContentResolver): String {
        val bytes = convertUriToBytesArray(uri, contentResolver)

        // with optimisation
        return Base64.encodeToString(bytes, Base64.DEFAULT)

        // without optimisation
        // return bytes?.let {
        //     Base64.encodeToString(it, Base64.DEFAULT)
        // } ?: ""
    }

    fun convertUriToBytesArray(uri: Uri, contentResolver: ContentResolver): ByteArray {
        val inputStream = contentResolver.openInputStream(uri)

        // with optimisation
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val resizedBitmap = resizeBitmapImage(bitmap, 300)

        val stream = ByteArrayOutputStream()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            resizedBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 50, stream)
        } else {
            resizedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream)
        }

        return stream.toByteArray()

        // without optimisation
        // return inputStream?.readBytes()
    }

    fun resizeBitmapImage(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        if (width <= maxSize && height <= maxSize) return bitmap

        val aspectRatio = width.toFloat() / height.toFloat()

        if (aspectRatio > 1) {
            width = maxSize
            height = (maxSize / aspectRatio).toInt()
        } else {
            height = maxSize
            width = (maxSize * aspectRatio).toInt()
        }

        return bitmap.scale(width, height, false)
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