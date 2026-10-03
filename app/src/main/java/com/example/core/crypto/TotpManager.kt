package com.example.core.crypto

import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object TotpManager {

    private const val TIME_STEP_SECONDS = 30L
    private const val CODE_DIGITS = 6

    fun getCurrentTotpCode(secretBase32: String, timeSeconds: Long = System.currentTimeMillis() / 1000L): String {
        if (secretBase32.isBlank()) return ""
        return try {
            val keyBytes = decodeBase32(secretBase32.trim().replace(" ", "").uppercase())
            val timeInterval = timeSeconds / TIME_STEP_SECONDS
            generateOtp(keyBytes, timeInterval, CODE_DIGITS)
        } catch (e: Exception) {
            "000000"
        }
    }

    fun getRemainingSeconds(timeSeconds: Long = System.currentTimeMillis() / 1000L): Int {
        val remainder = (timeSeconds % TIME_STEP_SECONDS).toInt()
        return (TIME_STEP_SECONDS.toInt() - remainder)
    }

    private fun generateOtp(key: ByteArray, counter: Long, digits: Int): String {
        val buffer = ByteBuffer.allocate(8).putLong(counter).array()
        val mac = Mac.getInstance("HmacSHA1").apply {
            init(SecretKeySpec(key, "RAW"))
        }
        val hash = mac.doFinal(buffer)
        val offset = (hash[hash.size - 1].toInt() and 0x0F)
        val binary = ((hash[offset].toInt() and 0x7F) shl 24) or
                ((hash[offset + 1].toInt() and 0xFF) shl 16) or
                ((hash[offset + 2].toInt() and 0xFF) shl 8) or
                (hash[offset + 3].toInt() and 0xFF)
        val otp = binary % Math.pow(10.0, digits.toDouble()).toInt()
        return otp.toString().padStart(digits, '0')
    }

    private fun decodeBase32(base32: String): ByteArray {
        val base32Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
        val cleanInput = base32.replace("=", "")
        var buffer = 0
        var bitsLeft = 0
        val bytes = mutableListOf<Byte>()

        for (c in cleanInput) {
            val value = base32Chars.indexOf(c)
            if (value < 0) continue
            buffer = (buffer shl 5) or value
            bitsLeft += 5
            if (bitsLeft >= 8) {
                bytes.add(((buffer shr (bitsLeft - 8)) and 0xFF).toByte())
                bitsLeft -= 8
            }
        }
        return bytes.toByteArray()
    }
}
