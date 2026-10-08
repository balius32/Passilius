package com.example.core.sync

import com.example.domain.sync.MergeResult
import com.example.domain.sync.SyncPairingInfo
import com.example.domain.sync.VaultSnapshot
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO as ClientCIO
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.header
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.concurrent.atomic.AtomicReference

private val syncJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

actual class SyncHost {
    actual val isSupported: Boolean = true

    private var server: EmbeddedServer<*, *>? = null
    private val activePairing = AtomicReference<SyncPairingInfo?>(null)
    private val mutex = Mutex()

    actual suspend fun start(
        onIncoming: suspend (VaultSnapshot) -> Pair<MergeResult, VaultSnapshot>
    ): SyncHostSession {
        stop()
        val addresses = localLanAddresses()
        val preferredHost = addresses.firstOrNull() ?: "127.0.0.1"
        val pairingHolder = activePairing

        val engine = embeddedServer(CIO, port = 0, host = "0.0.0.0") {
            routing {
                post("/sync") {
                    val pairing = pairingHolder.get()
                    if (pairing == null) {
                        call.respond(HttpStatusCode.ServiceUnavailable, "not ready")
                        return@post
                    }
                    val provided = call.request.header("X-Passilius-Token").orEmpty()
                    if (!constantTimeEquals(provided, pairing.token.toHex())) {
                        delay(1000)
                        call.respond(HttpStatusCode.Forbidden, "invalid token")
                        return@post
                    }
                    try {
                        val body = call.receiveText()
                        val plain = SyncEnvelope.decrypt(pairing.token, body)
                        val incoming = syncJson.decodeFromString(
                            VaultSnapshot.serializer(),
                            plain.decodeToString()
                        )
                        val (_, responseSnapshot) = onIncoming(incoming)
                        val responseBody = SyncEnvelope.encrypt(
                            pairing.token,
                            syncJson.encodeToString(VaultSnapshot.serializer(), responseSnapshot)
                                .encodeToByteArray()
                        )
                        call.respondText(responseBody, ContentType.Text.Plain, HttpStatusCode.OK)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, e.message ?: "sync failed")
                    }
                }
            }
        }
        engine.start(wait = false)
        val port = engine.engine.resolvedConnectors().first().port
        val info = SyncPairingInfo.create(host = preferredHost, port = port)
        activePairing.set(info)
        server = engine
        return SyncHostSession(
            pairing = info,
            lanAddresses = addresses.ifEmpty { listOf(preferredHost) }
        )
    }

    actual suspend fun stop() {
        mutex.withLock {
            server?.stop(gracePeriodMillis = 200, timeoutMillis = 1000)
            server = null
            activePairing.set(null)
        }
    }
}

actual class SyncClient {
    actual suspend fun exchange(
        pairing: SyncPairingInfo,
        localSnapshot: VaultSnapshot
    ): VaultSnapshot {
        val client = HttpClient(ClientCIO)
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

private fun constantTimeEquals(a: String, b: String): Boolean {
    if (a.length != b.length) return false
    var result = 0
    for (i in a.indices) {
        result = result or (a[i].code xor b[i].code)
    }
    return result == 0
}

private fun localLanAddresses(): List<String> {
    val result = mutableListOf<String>()
    val interfaces = NetworkInterface.getNetworkInterfaces() ?: return result
    for (nic in interfaces) {
        if (!nic.isUp || nic.isLoopback) continue
        for (addr in nic.inetAddresses) {
            if (addr is Inet4Address && !addr.isLoopbackAddress) {
                result += addr.hostAddress
            }
        }
    }
    return result.distinct()
}
