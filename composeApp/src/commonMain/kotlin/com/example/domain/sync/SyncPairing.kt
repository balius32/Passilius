package com.example.domain.sync

import com.example.core.platform.base64UrlToBytes
import com.example.core.platform.bytesToBase64Url
import com.example.core.platform.newSyncId
import com.example.core.platform.secureRandomBytes

data class SyncPairingInfo(
    val host: String,
    val port: Int,
    val token: ByteArray,
    val sessionId: String
) {
    fun toUri(): String =
        "passilius://sync?host=$host&port=$port&token=${bytesToBase64Url(token)}&sessionId=$sessionId"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SyncPairingInfo) return false
        return host == other.host &&
            port == other.port &&
            sessionId == other.sessionId &&
            token.contentEquals(other.token)
    }

    override fun hashCode(): Int {
        var result = host.hashCode()
        result = 31 * result + port
        result = 31 * result + token.contentHashCode()
        result = 31 * result + sessionId.hashCode()
        return result
    }

    companion object {
        fun create(host: String, port: Int): SyncPairingInfo =
            SyncPairingInfo(
                host = host,
                port = port,
                token = secureRandomBytes(32),
                sessionId = newSyncId()
            )

        fun parseUri(uri: String): SyncPairingInfo? {
            val trimmed = uri.trim()
            if (!trimmed.startsWith("passilius://sync")) return null
            val query = trimmed.substringAfter('?', missingDelimiterValue = "")
            if (query.isBlank()) return null
            val params = query.split('&').mapNotNull { part ->
                val idx = part.indexOf('=')
                if (idx <= 0) null else part.substring(0, idx) to part.substring(idx + 1)
            }.toMap()
            val host = params["host"]?.takeIf { it.isNotBlank() } ?: return null
            val port = params["port"]?.toIntOrNull() ?: return null
            val tokenB64 = params["token"]?.takeIf { it.isNotBlank() } ?: return null
            val sessionId = params["sessionId"]?.takeIf { it.isNotBlank() } ?: return null
            return try {
                SyncPairingInfo(
                    host = host,
                    port = port,
                    token = base64UrlToBytes(tokenB64),
                    sessionId = sessionId
                )
            } catch (_: Exception) {
                null
            }
        }
    }
}
