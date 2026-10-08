package com.example.core.platform

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

fun newSyncId(): String {
    val bytes = secureRandomBytes(16)
    bytes[6] = ((bytes[6].toInt() and 0x0f) or 0x40).toByte()
    bytes[8] = ((bytes[8].toInt() and 0x3f) or 0x80).toByte()
    val hex = bytes.joinToString("") { b ->
        ((b.toInt() and 0xff) + 0x100).toString(16).substring(1)
    }
    return "${hex.substring(0, 8)}-${hex.substring(8, 12)}-${hex.substring(12, 16)}-${hex.substring(16, 20)}-${hex.substring(20)}"
}

@OptIn(ExperimentalEncodingApi::class)
fun bytesToBase64Url(bytes: ByteArray): String =
    Base64.UrlSafe.encode(bytes).trimEnd('=')

@OptIn(ExperimentalEncodingApi::class)
fun base64UrlToBytes(value: String): ByteArray {
    val padded = when (value.length % 4) {
        2 -> "$value=="
        3 -> "$value="
        else -> value
    }
    return Base64.UrlSafe.decode(padded)
}
