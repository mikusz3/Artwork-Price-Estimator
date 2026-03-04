package com.mikusz3.artworkpriceestimator.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.max

object ImageBase64Encoder {
    suspend fun encode(context: Context, uri: Uri): String = withContext(Dispatchers.IO) {
        val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, boundsOptions)
        } ?: throw IOException("Could not read selected image.")

        val maxDimension = 1024
        val sourceWidth = boundsOptions.outWidth
        val sourceHeight = boundsOptions.outHeight
        if (sourceWidth <= 0 || sourceHeight <= 0) {
            throw IOException("Invalid image selected.")
        }

        var sampleSize = 1
        while (sourceWidth / sampleSize > maxDimension || sourceHeight / sampleSize > maxDimension) {
            sampleSize *= 2
        }

        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = max(1, sampleSize)
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val bitmap = context.contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, decodeOptions)
        } ?: throw IOException("Could not decode selected image.")

        val scaledBitmap = if (bitmap.width > maxDimension || bitmap.height > maxDimension) {
            val ratio = minOf(
                maxDimension.toFloat() / bitmap.width.toFloat(),
                maxDimension.toFloat() / bitmap.height.toFloat()
            )
            Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * ratio).toInt(),
                (bitmap.height * ratio).toInt(),
                true
            )
        } else {
            bitmap
        }

        val output = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, output)
        if (scaledBitmap != bitmap) bitmap.recycle()
        bitmap.recycle()
        Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
    }
}
