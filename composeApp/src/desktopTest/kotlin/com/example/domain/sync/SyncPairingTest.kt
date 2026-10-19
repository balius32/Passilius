package com.example.domain.sync

import com.example.core.platform.bytesToBase64Url
import com.example.core.sync.SyncEnvelope
import com.example.core.sync.isLocalSyncHost
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SyncPairingTest {

    @Test
    fun pairingUriRoundTrip() {
        val original = SyncPairingInfo.create(host = "192.168.1.20", port = 45678)
        val parsed = SyncPairingInfo.parseUri(original.toUri())
        assertNotNull(parsed)
        assertEquals(original.host, parsed.host)
        assertEquals(listOf("192.168.1.20"), parsed.hosts)
        assertEquals(original.port, parsed.port)
        assertEquals(original.sessionId, parsed.sessionId)
        assertContentEquals(original.token, parsed.token)
        assertTrue(original.toUri().contains("hosts=192.168.1.20"))
    }

    @Test
    fun multiHostUriRoundTrip() {
        val original = SyncPairingInfo.create(
            hosts = listOf("192.168.1.20", "192.168.42.129"),
            port = 45678
        )
        val parsed = SyncPairingInfo.parseUri(original.toUri())
        assertNotNull(parsed)
        assertEquals(listOf("192.168.1.20", "192.168.42.129"), parsed.hosts)
        assertEquals("192.168.1.20", parsed.host)
        assertContentEquals(original.token, parsed.token)
    }

    @Test
    fun legacySingleHostUriParses() {
        val original = SyncPairingInfo.create(host = "10.0.0.8", port = 9)
        val legacy = "passilius://sync?host=10.0.0.8&port=9" +
            "&token=${bytesToBase64Url(original.token)}&sessionId=${original.sessionId}"
        val parsed = SyncPairingInfo.parseUri(legacy)
        assertNotNull(parsed)
        assertEquals(listOf("10.0.0.8"), parsed.hosts)
        assertEquals(original.host, parsed.host)
        assertContentEquals(original.token, parsed.token)
    }

    @Test
    fun localSyncHostAllowsOnlyPrivateLanAddresses() {
        assertTrue(isLocalSyncHost("192.168.1.20"))
        assertTrue(isLocalSyncHost("10.0.0.8"))
        assertTrue(isLocalSyncHost("172.16.0.1"))
        assertTrue(isLocalSyncHost("172.31.255.255"))
        assertTrue(isLocalSyncHost("127.0.0.1"))
        assertTrue(isLocalSyncHost("169.254.1.1"))
        assertFalse(isLocalSyncHost("172.32.0.1"))
        assertFalse(isLocalSyncHost("8.8.8.8"))
        assertFalse(isLocalSyncHost("example.com"))
        assertFalse(isLocalSyncHost("192.168.1.20:8080"))
        assertFalse(isLocalSyncHost("192.168.001.20"))
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
