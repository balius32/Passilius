package com.example.core.sync

import com.example.domain.sync.MergeResult
import com.example.domain.sync.SyncPairingInfo
import com.example.domain.sync.VaultSnapshot
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json

private val syncJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

actual class SyncHost {
    actual val isSupported: Boolean = false

    actual suspend fun start(
        onIncoming: suspend (VaultSnapshot) -> Pair<MergeResult, VaultSnapshot>
    ): SyncHostSession {
        error("Sync hosting is only available on desktop")
    }

    actual suspend fun stop() = Unit
}

actual class SyncClient {
    actual suspend fun exchange(
        pairing: SyncPairingInfo,
        localSnapshot: VaultSnapshot
    ): VaultSnapshot {
        val client = HttpClient(CIO)
        try {
            val encrypted = SyncEnvelope.encrypt(
                pairing.token,
                syncJson.encodeToString(VaultSnapshot.serializer(), localSnapshot)
                    .encodeToByteArray()
            )
            val response = withTimeout(120_000) {
                client.post("http://${pairing.host}:${pairing.port}/sync") {
                    header("X-Passilius-Token", pairing.token.toHex())
                    contentType(ContentType.Text.Plain)
                    setBody(encrypted)
                }
            }
            if (response.status.value !in 200..299) {
                error("Sync rejected: HTTP ${response.status.value}")
            }
            val plain = SyncEnvelope.decrypt(pairing.token, response.bodyAsText())
            return syncJson.decodeFromString(VaultSnapshot.serializer(), plain.decodeToString())
        } finally {
            client.close()
        }
    }
}

private fun ByteArray.toHex(): String =
    joinToString("") { b -> ((b.toInt() and 0xff) + 0x100).toString(16).substring(1) }
