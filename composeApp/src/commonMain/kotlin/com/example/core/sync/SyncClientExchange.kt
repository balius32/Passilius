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
    val hosts = pairing.hosts.ifEmpty { listOf(pairing.host) }.distinct()
    if (hosts.isEmpty()) error("No sync address in the QR")
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

private fun ByteArray.toHex(): String =
    joinToString("") { b -> ((b.toInt() and 0xff) + 0x100).toString(16).substring(1) }
