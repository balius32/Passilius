package com.example.core.sync

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Bitmap as SkiaBitmap

actual fun QrImage.toImageBitmap(): ImageBitmap {
    val skia = SkiaBitmap()
    skia.allocPixels(ImageInfo.makeN32(sizePx, sizePx, ColorAlphaType.UNPREMUL))
    val bytes = ByteArray(sizePx * sizePx * 4)
    var i = 0
    for (pixel in argb) {
        // Skia N32 is typically BGRA on little-endian; argb is Android-style AARRGGBB
        bytes[i++] = (pixel and 0xFF).toByte() // B
        bytes[i++] = ((pixel shr 8) and 0xFF).toByte() // G
        bytes[i++] = ((pixel shr 16) and 0xFF).toByte() // R
        bytes[i++] = ((pixel shr 24) and 0xFF).toByte() // A
    }
    skia.installPixels(skia.imageInfo, bytes, sizePx * 4)
    return org.jetbrains.skia.Image.makeFromBitmap(skia).toComposeImageBitmap()
}
