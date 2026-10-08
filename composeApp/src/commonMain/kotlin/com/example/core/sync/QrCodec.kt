package com.example.core.sync

/** ARGB pixels for a square QR image (width == height == sizePx). */
data class QrImage(
    val sizePx: Int,
    val argb: IntArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QrImage) return false
        return sizePx == other.sizePx && argb.contentEquals(other.argb)
    }

    override fun hashCode(): Int {
        var result = sizePx
        result = 31 * result + argb.contentHashCode()
        return result
    }
}

expect object QrCodec {
    fun encode(content: String, sizePx: Int = 512): QrImage
}
