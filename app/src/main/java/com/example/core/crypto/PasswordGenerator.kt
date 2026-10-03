package com.example.core.crypto

import java.security.SecureRandom
import kotlin.math.log2
import kotlin.math.roundToInt

data class GeneratorConfig(
    val length: Int = 16,
    val includeUppercase: Boolean = true,
    val includeLowercase: Boolean = true,
    val includeNumbers: Boolean = true,
    val includeSymbols: Boolean = true,
    val excludeAmbiguous: Boolean = false
)

data class GeneratedSecret(
    val password: String,
    val entropyBits: Int,
    val strengthLevel: PasswordStrengthLevel
)

enum class PasswordStrengthLevel(val label: String, val score: Int) {
    WEAK("Weak", 1),
    FAIR("Fair", 2),
    STRONG("Strong", 3),
    VERY_STRONG("Military-Grade", 4)
}

object PasswordGenerator {

    private const val UPPERCASE = "ABCDEFGHJKLMNPQRSTUVWXYZ"
    private const val LOWERCASE = "abcdefghijkmnopqrstuvwxyz"
    private const val NUMBERS = "23456789"
    private const val SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?"
    private const val ALL_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val ALL_LOWERCASE = "abcdefghijklmnopqrstuvwxyz"
    private const val ALL_NUMBERS = "0123456789"

    private val secureRandom = SecureRandom()

    fun generate(config: GeneratorConfig): GeneratedSecret {
        val uppercasePool = if (config.excludeAmbiguous) UPPERCASE else ALL_UPPERCASE
        val lowercasePool = if (config.excludeAmbiguous) LOWERCASE else ALL_LOWERCASE
        val numbersPool = if (config.excludeAmbiguous) NUMBERS else ALL_NUMBERS
        val symbolsPool = SYMBOLS

        var pool = ""
        val mandatoryChars = mutableListOf<Char>()

        if (config.includeUppercase) {
            pool += uppercasePool
            mandatoryChars.add(uppercasePool[secureRandom.nextInt(uppercasePool.length)])
        }
        if (config.includeLowercase) {
            pool += lowercasePool
            mandatoryChars.add(lowercasePool[secureRandom.nextInt(lowercasePool.length)])
        }
        if (config.includeNumbers) {
            pool += numbersPool
            mandatoryChars.add(numbersPool[secureRandom.nextInt(numbersPool.length)])
        }
        if (config.includeSymbols) {
            pool += symbolsPool
            mandatoryChars.add(symbolsPool[secureRandom.nextInt(symbolsPool.length)])
        }

        if (pool.isEmpty()) {
            pool = lowercasePool + numbersPool
        }

        val resultChars = mutableListOf<Char>()
        resultChars.addAll(mandatoryChars)

        while (resultChars.size < config.length) {
            resultChars.add(pool[secureRandom.nextInt(pool.length)])
        }

        // Shuffle securely
        for (i in resultChars.size - 1 downTo 1) {
            val j = secureRandom.nextInt(i + 1)
            val temp = resultChars[i]
            resultChars[i] = resultChars[j]
            resultChars[j] = temp
        }

        val password = resultChars.joinToString("")
        val entropy = calculateEntropy(password, pool.length)
        val strength = evaluateStrength(entropy)

        return GeneratedSecret(
            password = password,
            entropyBits = entropy,
            strengthLevel = strength
        )
    }

    fun calculateEntropy(password: String, poolSize: Int = 0): Int {
        if (password.isEmpty()) return 0
        var actualPool = poolSize
        if (actualPool <= 0) {
            var pool = 0
            if (password.any { it.isUpperCase() }) pool += 26
            if (password.any { it.isLowerCase() }) pool += 26
            if (password.any { it.isDigit() }) pool += 10
            if (password.any { !it.isLetterOrDigit() }) pool += 32
            actualPool = if (pool > 0) pool else 26
        }
        val bits = password.length * log2(actualPool.toDouble())
        return bits.roundToInt()
    }

    fun evaluateStrength(entropyBits: Int): PasswordStrengthLevel {
        return when {
            entropyBits < 45 -> PasswordStrengthLevel.WEAK
            entropyBits < 65 -> PasswordStrengthLevel.FAIR
            entropyBits < 90 -> PasswordStrengthLevel.STRONG
            else -> PasswordStrengthLevel.VERY_STRONG
        }
    }
}
