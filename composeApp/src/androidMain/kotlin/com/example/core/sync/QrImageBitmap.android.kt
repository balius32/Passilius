package com.example.core.sync

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual fun QrImage.toImageBitmap(): ImageBitmap {
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(argb, 0, sizePx, 0, 0, sizePx, sizePx)
    return bitmap.asImageBitmap()
}
