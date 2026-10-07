package com.example.core.platform

expect fun secureRandomBytes(size: Int): ByteArray

expect fun secureRandomInt(bound: Int): Int
