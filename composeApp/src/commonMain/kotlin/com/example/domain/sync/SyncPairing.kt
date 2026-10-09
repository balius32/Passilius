package com.example.domain.sync

import com.example.core.platform.base64UrlToBytes
import com.example.core.platform.bytesToBase64Url
import com.example.core.platform.newSyncId
import com.example.core.platform.secureRandomBytes

data class SyncPairingInfo(
    val host: String,
    val port: Int,
    val token: ByteArray,
    val sessionId: String,
    val hosts: List<String> = listOf(host)
) {
    fun toUri(): String {
        val advertised = hosts.ifEmpty { listOf(host) }.joinToString(",")
        return "passilius://sync?hosts=$advertised&port=$port&token=${bytesToBase64Url(token)}&sessionId=$sessionId"
    }

    fun withHosts(hosts: List<String>): SyncPairingInfo {
        val cleaned = cleanHosts(hosts)
        if (cleaned.isEmpty()) return this
        return copy(host = cleaned.first(), hosts = cleaned)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SyncPairingInfo) return false
        return host == other.host &&
            port == other.port &&
            sessionId == other.sessionId &&
            hosts == other.hosts &&
            token.contentEquals(other.token)
    }

    override fun hashCode(): Int {
        var result = host.hashCode()
        result = 31 * result + port
        result = 31 * result + token.contentHashCode()
        result = 31 * result + sessionId.hashCode()
        result = 31 * result + hosts.hashCode()
        return result
    }

    companion object {
        fun create(host: String, port: Int): SyncPairingInfo = create(listOf(host), port)

        fun create(hosts: List<String>, port: Int): SyncPairingInfo {
            val cleaned = cleanHosts(hosts)
            val primary = cleaned.firstOrNull() ?: "127.0.0.1"
            return SyncPairingInfo(
                host = primary,
                port = port,
                token = secureRandomBytes(32),
                sessionId = newSyncId(),
                hosts = cleaned.ifEmpty { listOf(primary) }
            )
        }

        fun parseUri(uri: String): SyncPairingInfo? {
            val trimmed = uri.trim()
            if (!trimmed.startsWith("passilius://sync")) return null
            val query = trimmed.substringAfter('?', missingDelimiterValue = "")
            if (query.isBlank()) return null
            val params = query.split('&').mapNotNull { part ->
                val idx = part.indexOf('=')
                if (idx <= 0) null else part.substring(0, idx) to part.substring(idx + 1)
            }.toMap()
            val hosts = cleanHosts(
                params["hosts"]?.split(',').orEmpty()
            ).ifEmpty {
                cleanHosts(listOfNotNull(params["host"]))
            }
            if (hosts.isEmpty()) return null
            val port = params["port"]?.toIntOrNull() ?: return null
            val tokenB64 = params["token"]?.takeIf { it.isNotBlank() } ?: return null
            val sessionId = params["sessionId"]?.takeIf { it.isNotBlank() } ?: return null
            return try {
                SyncPairingInfo(
                    host = hosts.first(),
                    port = port,
                    token = base64UrlToBytes(tokenB64),
                    sessionId = sessionId,
                    hosts = hosts
                )
            } catch (_: Exception) {
                null
            }
        }

        private fun cleanHosts(hosts: List<String>): List<String> =
            hosts.map { it.trim() }.filter { it.isNotBlank() }.distinct()
    }
}
