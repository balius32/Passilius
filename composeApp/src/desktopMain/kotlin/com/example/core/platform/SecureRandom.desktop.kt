package com.example.core.platform

import java.security.SecureRandom

private val random = SecureRandom()

actual fun secureRandomBytes(size: Int): ByteArray =
    ByteArray(size).also { random.nextBytes(it) }

actual fun secureRandomInt(bound: Int): Int = random.nextInt(bound)
