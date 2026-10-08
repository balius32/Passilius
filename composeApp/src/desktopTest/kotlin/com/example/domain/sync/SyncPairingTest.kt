package com.example.domain.sync

import com.example.core.sync.SyncEnvelope
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SyncPairingTest {

    @Test
    fun pairingUriRoundTrip() {
        val original = SyncPairingInfo.create(host = "192.168.1.20", port = 45678)
        val parsed = SyncPairingInfo.parseUri(original.toUri())
        assertNotNull(parsed)
        assertEquals(original.host, parsed.host)
        assertEquals(original.port, parsed.port)
        assertEquals(original.sessionId, parsed.sessionId)
        assertContentEquals(original.token, parsed.token)
    }

    @Test
    fun syncEnvelopeRoundTrip() {
        val token = ByteArray(32) { it.toByte() }
        val plain = """{"hello":"vault"}""".encodeToByteArray()
        val sealed = SyncEnvelope.encrypt(token, plain)
        val opened = SyncEnvelope.decrypt(token, sealed)
        assertContentEquals(plain, opened)
    }
}
