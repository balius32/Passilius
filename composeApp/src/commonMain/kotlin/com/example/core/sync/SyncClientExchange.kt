package com.example.core.sync

import com.example.domain.sync.SyncPairingInfo
import com.example.domain.sync.VaultSnapshot
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json

private val syncJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

class SyncRejectedException(message: String) : Exception(message)

suspend fun exchangeSnapshot(
    pairing: SyncPairingInfo,
    localSnapshot: VaultSnapshot
): VaultSnapshot {
    val hosts = pairing.hosts.ifEmpty { listOf(pairing.host) }
        .distinct()
        .filter(::isLocalSyncHost)
    if (hosts.isEmpty()) error("Sync only connects to a device on the local network")
    val encrypted = SyncEnvelope.encrypt(
        pairing.token,
        syncJson.encodeToString(VaultSnapshot.serializer(), localSnapshot).encodeToByteArray()
    )
    var lastError: Exception? = null
    for (host in hosts) {
        val client = HttpClient(CIO) {
            install(HttpTimeout) {
                connectTimeoutMillis = 4_000
                requestTimeoutMillis = 120_000
                socketTimeoutMillis = 120_000
            }
        }
        try {
            val response = client.post("http://$host:${pairing.port}/sync") {
                header("X-Passilius-Token", pairing.token.toHex())
                contentType(ContentType.Text.Plain)
                setBody(encrypted)
            }
            if (response.status == HttpStatusCode.Forbidden || response.status.value !in 200..299) {
                throw SyncRejectedException("Sync rejected: HTTP ${response.status.value}")
            }
            val plain = try {
                SyncEnvelope.decrypt(pairing.token, response.bodyAsText())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                throw SyncRejectedException(e.message ?: "Sync failed")
            }
            return syncJson.decodeFromString(VaultSnapshot.serializer(), plain.decodeToString())
        } catch (e: CancellationException) {
            throw e
        } catch (e: SyncRejectedException) {
            throw e
        } catch (e: Exception) {
            lastError = e
        } finally {
            client.close()
        }
    }
    throw lastError ?: IllegalStateException("Could not reach the computer")
}

fun isLocalSyncHost(host: String): Boolean {
    val parts = host.split('.')
    if (parts.size != 4 || parts.any { it.length > 1 && it.startsWith('0') }) return false
    val octets = parts.map { part -> part.toIntOrNull() ?: return false }
    if (octets.any { it !in 0..255 }) return false
    val first = octets[0]
    val second = octets[1]
    return when {
        first == 10 -> true
        first == 127 -> true
        first == 192 && second == 168 -> true
        first == 172 && second in 16..31 -> true
        first == 169 && second == 254 -> true
        else -> false
    }
}

private fun ByteArray.toHex(): String =
    joinToString("") { b -> ((b.toInt() and 0xff) + 0x100).toString(16).substring(1) }
